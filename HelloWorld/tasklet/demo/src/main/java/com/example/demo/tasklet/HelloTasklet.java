package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component("HelloTasklet")
@StepScope
public class HelloTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    System.out.println("Hello World");

    ExecutionContext jobContext = contribution
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext(); // JobExecutionContext(別stepに値を渡せる)

    jobContext.put("jobKey", "jobValue");

    ExecutionContext stepContext = contribution
            .getStepExecution()
            .getExecutionContext(); // StepExecutionContext

    stepContext.put("stepKey", "stepValue");

    return RepeatStatus.FINISHED;
  }
}
