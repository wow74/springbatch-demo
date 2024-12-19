package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RetryReader implements ItemReader<String> {

  private String[] input = {"Hello", "World", null};
  private int index = 0;

  @Override
  public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    String msg = input[index++];
    System.out.println("Raad:" + msg);
    return msg;
  }
}
