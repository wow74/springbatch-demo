package com.example.demo.config;

import com.example.demo.tasklet.HelloTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  private HelloTasklet helloTasklet;

  // spring3系
  @Bean
  public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("HelloStep", jobRepository)
            .tasklet(helloTasklet, transactionManager)
            .build(); // Stepの生成
  }

  // spring3系
  @Bean
  public Job sampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("HelloJob", jobRepository)
            .incrementer(new RunIdIncrementer()) // IDのインクリメント
            .start(sampleStep(jobRepository, transactionManager)) // 最初のstep
            .build();  // Jobの生成
  }
}
