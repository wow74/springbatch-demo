package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class HelloWriter implements ItemWriter<String> {

  @Override
  public void write(Chunk<? extends String> items) throws Exception {
    System.out.println("writer:" + items);
    System.out.println("===============");
  }
}