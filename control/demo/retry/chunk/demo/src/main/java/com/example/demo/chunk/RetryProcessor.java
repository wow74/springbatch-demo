package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RetryProcessor implements ItemProcessor<String, String> {

  @Value("${retry.num}")
  private Integer retryNum;

  private int count = 1;

  @Override
  public String process(String item) throws Exception {
    if ("World".equals(item) && (retryNum > count)) {
      count++;
      throw new Exception("Retry Test");
    }

    item += "[-]";
    System.out.println("Processor:" + item);
    return item;
  }
}
