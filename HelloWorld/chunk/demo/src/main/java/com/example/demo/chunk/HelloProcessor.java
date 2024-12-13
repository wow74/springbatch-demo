package com.example.demo.chunk;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
// ItemProcessor<in, out>
public class HelloProcessor implements ItemProcessor<String, String> {

  @Value("#{JobExecutionContext['jobKey']}")
  private String jobValue;

  @Value("#{StepExecutionContext['stepKey']}")
  private String stepValue;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    System.out.println("jobKey: " + jobValue);
    System.out.println("stepValue: " + stepValue);

  }

  @Override
  public String process(String item) throws Exception {
    item += "[.]";
    System.out.println("Processor: " + item);
    return item;
  }
}
