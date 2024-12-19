package com.example.demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component("TaskletStepListener")
public class TaskletStepListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {

  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    System.out.println(stepExecution.toString());
    return stepExecution.getExitStatus();
  }
}
