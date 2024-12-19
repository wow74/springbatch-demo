package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  private Tasklet retryTasklet;

  @Bean
  public Step retryTaskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("RetryTaskletStep", jobRepository)
            .tasklet(retryTasklet, transactionManager)
            .build();
  }

  @Bean
  public Job retryTaskletJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("RetryTaskletJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(retryTaskletStep(jobRepository, transactionManager))
            .build();
  }
}
