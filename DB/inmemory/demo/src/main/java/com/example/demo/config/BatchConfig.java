package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.h2")
  DataSourceProperties h2Properties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("spring.datasource.mysql")
  DataSourceProperties mysqlProperties() {
    return new DataSourceProperties();
  }

  @BatchDataSource
  @Bean
  DataSource h2DataSource() {
    return h2Properties()
            .initializeDataSourceBuilder()
            .build();
  }

  @Primary
  @Bean
  DataSource mysqlDataSource() {
    return mysqlProperties()
            .initializeDataSourceBuilder()
            .build();
  }

  @Bean
  JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(mysqlDataSource());
  }

  @Bean
  Job sampleJob(JobRepository jobRepository, PlatformTransactionManager mainTxManager) {
    System.out.println(mainTxManager);
    Step step = new StepBuilder("sampleStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
              System.out.println("asdf");
              jdbcTemplate().update("insert into Employee(id, name, age, gender) values (1, 'user', 20, 1)");

              return RepeatStatus.FINISHED;
            }, mainTxManager)
            .build();

    return new JobBuilder("sampleJob", jobRepository)
            .start(step)
            .build();
  }
}
