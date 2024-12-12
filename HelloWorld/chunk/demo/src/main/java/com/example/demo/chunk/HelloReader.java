package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class HelloReader implements ItemReader<String> {

  // nullが返ると処理が終了となる
  private String[] input = {"Hello", "World", null, "No Read"};
  private int index = 0;

  @Override
  public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    String message = input[index++];
    System.out.println("Read: " + message);
    return message;
  }
}
