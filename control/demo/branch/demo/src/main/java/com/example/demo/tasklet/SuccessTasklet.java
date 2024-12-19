package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component("SuccessTasklet")
@StepScope
public class SuccessTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    System.out.println("SuccessTasklet");
    return RepeatStatus.FINISHED;
  }


}
