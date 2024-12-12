package com.example.demo.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HelloJobListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    System.out.println("Before Job: " + jobExecution);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    System.out.println("After Job: " + jobExecution);
  }
}
