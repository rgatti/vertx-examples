package org.example.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompositeMessageCodecTest {

  static final String JSON_DATA = "{\"compositeBody\":\"My message:\",\"customMessage1\":{\"body\":\"Hello\"},\"customMessage2\":{\"body\":\"World\"}}";
  static final int JSON_SIZE = 99;

  CompositeMessageCodec codec = new CompositeMessageCodec();
  Buffer buffer;

  @BeforeEach void setup() {
    buffer = new BufferImpl();
  }

  @Test
  void encoding() {
    codec.encodeToWire(buffer, newCompositeMessage());
    int length = buffer.getInt(0);
    String data = buffer.getString(4, length + 4);
    assertEquals(JSON_SIZE, length);
    assertEquals(JSON_DATA, data);
  }

  @Test
  void decoding() {
    buffer.appendInt(JSON_SIZE);
    buffer.appendString(JSON_DATA);
    CompositeMessage compositeMessage = codec.decodeFromWire(0, buffer);

    // Can use assertEquals because both CustomMessage and CompositeMessage override equals()
    assertEquals(newCompositeMessage(), compositeMessage);
  }

  private CompositeMessage newCompositeMessage() {
    CompositeMessage compositeMessage = new CompositeMessage();
    CustomMessage customMessage1 = new CustomMessage();
    customMessage1.setBody("Hello");
    CustomMessage customMessage2 = new CustomMessage();
    customMessage2.setBody("World");
    compositeMessage.setCompositeBody("My message:");
    compositeMessage.setCustomMessage1(customMessage1);
    compositeMessage.setCustomMessage2(customMessage2);

    return compositeMessage;
  }
}

/*
CompositeMessage{compositeBody='My message:', customMessage1=CustomMessage{body='Hello'}, customMessage2=CustomMessage{body='World'}}>
CompositeMessage{compositeBody='My message:', customMessage1=CustomMessage{body='Hello'}, customMessage2=CustomMessage{body='World'}}>

*/