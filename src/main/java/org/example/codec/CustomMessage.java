package org.example.codec;

import java.util.Objects;

/* A simple bean used as a custom message to be passed on the event bus. There's only the default
 * constructor and standard setter/getter to make it easier to use the Jackson library.
 */
public class CustomMessage {
  private String body;

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomMessage that = (CustomMessage) o;
    return Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body);
  }

  @Override
  public String toString() {
    return "CustomMessage{" +
        "body='" + body + '\'' +
        '}';
  }
}
