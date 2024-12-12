package com.example.demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HelloStepListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {
    System.out.println("Before Step: " + stepExecution);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    System.out.println("After Step: " + stepExecution);
    return stepExecution.getExitStatus();
  }
}
