package org.example.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import java.util.concurrent.TimeUnit;

public class AsyncFuture extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    // Start three futures
    //
    // These will actually begin the function calls and once complete the promise will be updated
    Future<String> foo = Future.future(promise -> blockingCall("foo", promise));
    Future<String> bar = Future.future(promise -> blockingCall("bar", promise));
    Future<String> baz = Future.future(promise -> blockingCall("baz", promise));

    // Run all three futures and collect the result of the promises
    CompositeFuture.join(foo, bar, baz)
        // Run a generic handler in any scenario
        .setHandler(e -> System.out.println("In handler"))
        // Chain a handler that only runs if _all_ futures are successful
        .onSuccess(e -> {
          System.out.println("Success handler");

          System.out.println(e.resultAt(0).toString());
        })
        // Chain a handler that runs if _any_ future fails
        .onFailure(e -> {
          System.out.println("Failure handler");
          e.printStackTrace();
        })
        // Chain a final handler to let the Runner know this verticle is done
        .setHandler(e -> startPromise.complete());

    // This will print before the blockingCall futures return
    System.out.println("Done...waiting for future");
  }

  // A function that blocks for some seconds before returning.
  private void blockingCall(String name, Handler<AsyncResult<String>> handler) {
    System.out.println("Starting " + name);

    // Create arbitrary delay in thread from the worker pool
    vertx.executeBlocking(promise -> {
          try {
            // delay
            TimeUnit.SECONDS.sleep(3);
            // mark task finished successfully, return the name
            promise.complete(name);
          } catch (InterruptedException e) {
            // mark task as failed
            promise.fail("InterruptedException");
          }
        },
        true,
        handler);
  }
}
