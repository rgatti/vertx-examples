package org.example.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class CustomMessageCodec implements MessageCodec<CustomMessage,CustomMessage>{

  // When a message is not being sent over a local event bus
  @Override
  public void encodeToWire(Buffer buffer, CustomMessage customMessage) {
    // Create new JSON object to hold data
    JsonObject object = new JsonObject();
    object.put("message", customMessage.getBody());

    // Encode json as string and get byte length
    String data = object.encode();
    int length = data.getBytes().length;

    // Add to buffer
    buffer.appendInt(length);
    buffer.appendString(data);
  }

  // When a message is not being sent over a local event bus
  @Override
  public CustomMessage decodeFromWire(int pos, Buffer buffer) {
    // Read byte length and string data from buffer
    //
    // There is 4 byte offset when reading the string because of the initial int holding the length.
    int length = buffer.getInt(pos);
    String data = buffer.getString(pos + 4, length + 4);

    // Convert string to JSON object and extract data
    JsonObject object = new JsonObject(data);
    String msg = object.getString("message");

    // Rebuild class
    CustomMessage message = new CustomMessage();
    message.setBody(msg);

    return message;
  }

  // When a message is being passed over a local event bus
  @Override
  public CustomMessage transform(CustomMessage customMessage) {
    return customMessage;
  }

  // Unique name for this codec
  @Override
  public String name() {
    return this.getClass().getSimpleName();
  }

  // All user codecs must return -1
  @Override
  public byte systemCodecID() {
    return -1; // for user codec
  }
}
