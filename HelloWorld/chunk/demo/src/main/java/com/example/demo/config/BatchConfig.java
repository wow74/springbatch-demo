package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  private ItemReader<String> reader;

  @Autowired
  private ItemProcessor<String, String> processor;

  @Autowired
  private ItemWriter<String> writer;

  @Bean
  public Step ChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("HelloChunkStep", jobRepository)
            .<String, String>chunk(2, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
  }

  @Bean
  public Job ChunkJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("HelloCHunkJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(ChunkStep(jobRepository, transactionManager))
            .build();
  }
}
