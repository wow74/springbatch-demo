package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RetryTasklet implements Tasklet {

  @Value("${retry.num}")
  private Integer retryNum;

  private int count = 0;

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
    while (retryNum > count) {
      System.out.println("count=" + ++count);

      return RepeatStatus.CONTINUABLE;
    }
    return RepeatStatus.FINISHED;
  }
}
