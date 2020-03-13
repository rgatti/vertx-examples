package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import java.util.List;
import java.util.Scanner;
import org.example.async.AsyncFuture;
import org.example.codec.SimpleCodecExample;

public class Runner {

  public static final List<Class<? extends AbstractVerticle>> examples = List.of(
      AsyncFuture.class,
      SimpleCodecExample.class
  );

  public static void main(String[] args) {
    System.out.println("Run example...");
    for (int i = 0; i < examples.size(); i++) {
      System.out.println(i + ": " + examples.get(i).getSimpleName());
    }
    System.out.println("?");

    Scanner scanner = new Scanner(System.in);
    int choice = scanner.nextInt();

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(examples.get(choice).getName(), event -> vertx.close());
  }
}
