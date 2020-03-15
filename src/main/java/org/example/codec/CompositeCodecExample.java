package org.example.codec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

public class CompositeCodecExample extends AbstractVerticle {

  public static final String CONSUMER_ADDRESS = "example";

  @Override
  public void start(Promise<Void> startPromise) {
    // Register the codec as a default for the message class
    vertx.eventBus().registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());
    vertx.eventBus().registerDefaultCodec(CompositeMessage.class, new CompositeMessageCodec());

    // Add a consumer on the even bus
    vertx.eventBus().<CompositeMessage>consumer(CONSUMER_ADDRESS, message -> {
      CompositeMessage compositeMessage = message.body();
      System.out.println("Message: " + compositeMessage);

      CustomMessage replyMessage = new CustomMessage();
      replyMessage.setBody("Got your message!");

      message.reply(replyMessage);
    });

    // Send a message
    CompositeMessage compositeMessage = new CompositeMessage();
    CustomMessage customMessage1 = new CustomMessage();
    customMessage1.setBody("Hello");
    CustomMessage customMessage2 = new CustomMessage();
    customMessage2.setBody("World");
    compositeMessage.setCompositeBody("My message:");
    compositeMessage.setCustomMessage1(customMessage1);
    compositeMessage.setCustomMessage2(customMessage2);

    System.out.println(Json.encode(compositeMessage).getBytes().length);

    vertx.eventBus().<CustomMessage>request(CONSUMER_ADDRESS, compositeMessage, asyncResult -> {
      if (asyncResult.succeeded()) {
        Message<CustomMessage> replyMessage = asyncResult.result();
        System.out.println("Reply: " + replyMessage.body().getBody());
      } else {
        System.out.println("Something went wrong!");
        asyncResult.cause().printStackTrace();
      }

      startPromise.complete();
    });
  }
}