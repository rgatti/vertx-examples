package org.example.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class CompositeMessageCodec implements MessageCodec<CompositeMessage, CompositeMessage> {

  @Override
  public void encodeToWire(Buffer buffer, CompositeMessage compositeMessage) {
    // In order to encode a composite message we either need to
    //   1. encode the message as a whole
    //   2. wrap encoded child messages in a container we can read when decoding
    //
    // In this example I will use the fact that the messages are in bean form and serializable by
    // the Jackson library.
    String data = Json.encode(compositeMessage);
    int length = data.getBytes().length;
    buffer.appendInt(length);
    buffer.appendString(data);
  }

  @Override
  public CompositeMessage decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    String data = buffer.getString(pos + 4, length + 4);
    return Json.decodeValue(data, CompositeMessage.class);
  }

  @Override
  public CompositeMessage transform(CompositeMessage compositeMessage) {
    return compositeMessage;
  }

  @Override
  public String name() {
    return this.getClass().getSimpleName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
