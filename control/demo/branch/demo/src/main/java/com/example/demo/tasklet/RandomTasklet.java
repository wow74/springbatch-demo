package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("RandomTasklet")
@StepScope
public class RandomTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    Random random = new Random();
    int randomValue = random.nextInt(2);
    System.out.println("randomValue=" + randomValue);

    ExecutionContext stepExecutionContext = stepContribution.getStepExecution().getExecutionContext();
    stepExecutionContext.put("randomValue", randomValue);
    return RepeatStatus.FINISHED;
  }
}
