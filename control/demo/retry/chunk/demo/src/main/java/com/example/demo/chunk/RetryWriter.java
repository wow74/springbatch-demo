package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RetryWriter implements ItemWriter<String> {

  @Override
  public void write(Chunk<? extends String> items) throws Exception {
    items.forEach(item -> System.out.println("Writer: " + item));
  }
}
