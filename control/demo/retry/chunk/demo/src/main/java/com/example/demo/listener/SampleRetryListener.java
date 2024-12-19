package com.example.demo.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
public class SampleRetryListener implements RetryListener {

  @Override
  // リトライ前の処理
  public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
    return true;
  }

  @Override
  // リトライ後の処理
  public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

  }

  @Override
  // リトライ失敗時の処理
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    System.out.println("Retry OnError: count=" + context.getRetryCount() + ", msg=" + throwable.getMessage());
  }
}
