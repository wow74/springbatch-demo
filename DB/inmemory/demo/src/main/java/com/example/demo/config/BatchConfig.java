package com.example.demo.config;

import com.example.demo.domain.Employee;
import com.example.demo.chunk.EmployeeReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

  @Autowired
  private DataSource mysqlDataSource;

  @Autowired
  private ItemReader<Employee> employeeReader;

//  public BatchConfig(DataSource mysqlDataSource, EmployeeReader employeeReader) {
//    this.mysqlDataSource = mysqlDataSource;
//    this.employeeReader = employeeReader;
//  }

  private static final String INSERT_EMPLOYEE_SQL = "insert into employee (id, name, age, gender)"
          + "values(:id, :name, :age, :gender)";

  @Bean
  @StepScope
  public JdbcBatchItemWriter<Employee> jdbcWriter() {
    return new JdbcBatchItemWriterBuilder<Employee>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>())
            .sql(INSERT_EMPLOYEE_SQL)
            .dataSource(this.mysqlDataSource)
            .build();
  }

  @Bean
  public Step inMemoryStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("InMemoryStep", jobRepository)
            .<Employee, Employee>chunk(1, transactionManager)
            .reader(employeeReader)
            .writer(jdbcWriter())
            .build();
  }

  @Bean
  public Job inMemoryJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("InMemoryJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(inMemoryStep(jobRepository, transactionManager))
            .build();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(mysqlDataSource);
  }
}
