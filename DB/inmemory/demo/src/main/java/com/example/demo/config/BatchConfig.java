package com.example.demo.config;

import com.example.demo.chunk.EmployeeReader;
import com.example.demo.domain.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private EmployeeReader employeeReader;

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

  // メタ情報
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

  private static final String SQL = "insert into Employee(id, name, age, gender)"
          + " values(:id, :name, :age, :gender)";

  @Bean
  @StepScope
  public JdbcBatchItemWriter<Employee> jdbcWriter() {
    return new JdbcBatchItemWriterBuilder<Employee>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(SQL)
            .dataSource(mysqlDataSource())
            .build();
  }

  @Bean
  public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("sampleStep", jobRepository)
            .<Employee, Employee>chunk(1, transactionManager)
            .reader(employeeReader)
            .writer(jdbcWriter())
            .build();
  }

  @Bean
  public Job sampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("sampleJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(sampleStep(jobRepository, transactionManager))
            .build();
  }
}
