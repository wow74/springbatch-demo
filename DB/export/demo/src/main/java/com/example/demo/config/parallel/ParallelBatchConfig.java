package com.example.demo.config.parallel;

import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ParallelBatchConfig extends BaseConfig {

  @Autowired
  private JdbcPagingItemReader<Employee> jdbcPagingItemReader;

  @Bean
  public TaskExecutor asyncTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(3); // 同時実行数
    executor.setMaxPoolSize(3); // 最大実行数
    executor.setQueueCapacity(0); // キューを使用しない
    executor.setThreadNamePrefix("parallel_");
    executor.initialize();

    return executor;
  }

  @Bean
  public Step exportParallelStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("ExportParallelStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(jdbcPagingItemReader).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .taskExecutor(asyncTaskExecutor())
            .build();
  }

  @Bean
  public Job exportParallelJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("ExportParallelJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(exportParallelStep(jobRepository, transactionManager))
            .build();
  }
}
