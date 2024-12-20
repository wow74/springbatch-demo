package com.example.demo.config;

import com.example.demo.tasklet.FirstTasklet;
import com.example.demo.tasklet.SecondTasklet;
import com.example.demo.tasklet.ThirdTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  @Qualifier("FirstTasklet")
  private FirstTasklet firstTasklet;

  @Autowired
  @Qualifier("SecondTasklet")
  private SecondTasklet secondTasklet;

  @Autowired
  @Qualifier("ThirdTasklet")
  private ThirdTasklet thirdTasklet;

  @Bean
  public Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("firstStep", jobRepository)
            .tasklet(firstTasklet, transactionManager)
            .build();

  }

  @Bean
  public Step secondStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("secondStep", jobRepository)
            .tasklet(secondTasklet, transactionManager)
            .build();

  }

  @Bean
  public Step thirdStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("thirdStep", jobRepository)
            .tasklet(thirdTasklet, transactionManager)
            .build();
  }

  @Bean
  public Flow firstFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new FlowBuilder<SimpleFlow>("FirstFlow")
            .start(firstStep(jobRepository, transactionManager))
            .build();
  }

  @Bean
  public Flow secondFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new FlowBuilder<SimpleFlow>("SecondFlow")
            .start(secondStep(jobRepository, transactionManager))
            .build();
  }

  @Bean
  public Flow thirdFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new FlowBuilder<SimpleFlow>("ThirdFlow")
            .start(thirdStep(jobRepository, transactionManager))
            .build();
  }

  @Bean
  public TaskExecutor asyncTaskExecutor() {
    return new SimpleAsyncTaskExecutor("concurrent_");
  }

  @Bean
  public Flow splitFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new FlowBuilder<SimpleFlow>("splitFlow")
            .split(asyncTaskExecutor())
            .add(secondFlow(jobRepository, transactionManager), thirdFlow(jobRepository, transactionManager)) // 同時実行
            .build();
  }

  @Bean
  public Job concurrentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("ConcurrentJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(firstFlow(jobRepository, transactionManager))
            .next(splitFlow(jobRepository, transactionManager))
            .build()
            .build();
  }
}
