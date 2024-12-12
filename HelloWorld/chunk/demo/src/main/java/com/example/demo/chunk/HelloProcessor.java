package com.example.demo.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
// ItemProcessor<in, out>
public class HelloProcessor implements ItemProcessor<String, String > {

  @Override
  public String process(String item) throws Exception {
    item += "[.]";
    System.out.println("Processor: " + item);
    return  item;
  }
}
