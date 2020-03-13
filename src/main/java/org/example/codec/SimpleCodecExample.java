package org.example.codec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;

/**
 * A simple example showing the MessageCodec.
 */
public class SimpleCodecExample extends AbstractVerticle {

  public static final String CONSUMER_ADDRESS = "example";

  @Override
  public void start(Promise<Void> startPromise) {
    // Register the codec as a default for the message class
    vertx.eventBus().registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

    // Add a consumer on the even bus
    vertx.eventBus().<CustomMessage>consumer(CONSUMER_ADDRESS, message -> {
      CustomMessage customMessage = message.body();
      System.out.println("Message: " + customMessage.getBody());

      CustomMessage replyMessage = new CustomMessage();
      replyMessage.setBody("Got your message!");

      message.reply(replyMessage);
    });

    // Send a message
    CustomMessage message = new CustomMessage();
    message.setBody("Hello world!");

    vertx.eventBus().<CustomMessage>request(CONSUMER_ADDRESS, message, asyncResult -> {
      if(asyncResult.succeeded()) {
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
