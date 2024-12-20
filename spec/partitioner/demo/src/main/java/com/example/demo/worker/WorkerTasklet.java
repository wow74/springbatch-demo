package com.example.demo.worker;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@StepScope
public class WorkerTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    Map<String, Object> context = chunkContext.getStepContext().getStepExecutionContext();

    String value = (String) context.get("sampleKey");
    System.out.println("value=" + value);
    return RepeatStatus.FINISHED;
  }
}
