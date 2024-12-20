package com.example.demo.config;

import com.example.demo.partitioner.SamplePartitioner;
import com.example.demo.worker.WorkerTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  private SamplePartitioner samplePartitioner;

  @Autowired
  private WorkerTasklet workerTasklet;

  @Bean
  public TaskExecutor asyncTaskExecutor() {
    return new SimpleAsyncTaskExecutor("worker_");
  }

  @Bean
  public Step workerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("WorkerStep", jobRepository)
            .tasklet(workerTasklet, transactionManager)
            .build();
  }

  @Bean
  public Step partitionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("PartitionStep", jobRepository)
            .partitioner("WorkerStep", samplePartitioner)
            .step(workerStep(jobRepository, transactionManager))
            .gridSize(3)
            .taskExecutor(asyncTaskExecutor())
            .build();

  }

  @Bean
  public Job partitionJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("PartitionJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(partitionStep(jobRepository, transactionManager))
            .build();
  }
}
