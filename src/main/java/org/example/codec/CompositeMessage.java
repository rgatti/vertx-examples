package org.example.codec;

import java.util.Objects;

public class CompositeMessage {
  private String compositeBody;
  private CustomMessage customMessage1;
  private CustomMessage customMessage2;

  public String getCompositeBody() {
    return compositeBody;
  }

  public void setCompositeBody(String compositeBody) {
    this.compositeBody = compositeBody;
  }

  public CustomMessage getCustomMessage1() {
    return customMessage1;
  }

  public void setCustomMessage1(CustomMessage customMessage1) {
    this.customMessage1 = customMessage1;
  }

  public CustomMessage getCustomMessage2() {
    return customMessage2;
  }

  public void setCustomMessage2(CustomMessage customMessage2) {
    this.customMessage2 = customMessage2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositeMessage that = (CompositeMessage) o;
    return Objects.equals(compositeBody, that.compositeBody) &&
        Objects.equals(customMessage1, that.customMessage1) &&
        Objects.equals(customMessage2, that.customMessage2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compositeBody, customMessage1, customMessage2);
  }

  @Override
  public String toString() {
    return "CompositeMessage{" +
        "compositeBody='" + compositeBody + '\'' +
        ", customMessage1=" + customMessage1 +
        ", customMessage2=" + customMessage2 +
        '}';
  }
}
