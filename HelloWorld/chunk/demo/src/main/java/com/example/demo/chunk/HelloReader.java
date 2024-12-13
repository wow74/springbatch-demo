package com.example.demo.chunk;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;


@Component
@StepScope
public class HelloReader implements ItemReader<String> {

  // nullが返ると処理が終了となる
  private String[] input = {"Hello", "World", null, "No Read"};
  private int index = 0;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    ExecutionContext jobContext = stepExecution
            .getJobExecution()
            .getExecutionContext();

    jobContext.put("jobKey", "jobValue");

    ExecutionContext stepContest = stepExecution
            .getExecutionContext();

    stepContest.put("stepKey", "stepValue");
  }

  @Override
  public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    String message = input[index++];
    System.out.println("Read: " + message);
    return message;
  }
}
