package com.example.demo.config.jdbc;

import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JdbcCursorBatchConfig extends BaseConfig {

  @Autowired
  private DataSource dataSource;

  private static final String SELECT_EMPLOYEE_SQL =
          "select * from employee where gender = ?";

  @Bean
  @StepScope
  public JdbcCursorItemReader<Employee> jdbcCursorReader() {
    Object[] params = new Object[]{1}; // クエリのパラメータ

    RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);

    return new JdbcCursorItemReaderBuilder<Employee>()
            .dataSource(this.dataSource)
            .name("jdbcCursorItemReader")
            .sql(SELECT_EMPLOYEE_SQL)
            .queryArguments(params)
            .rowMapper(rowMapper)
            .build();
  }

  @Bean
  public Step exportJdbcCursorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new StepBuilder("ExportJdbcCursorStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(jdbcCursorReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
  }

  @Bean("JdbcCursorJob")
  public Job exportJdbcCursorJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("ExportJdbcCursorJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(exportJdbcCursorStep(jobRepository, transactionManager))
            .build();

  }
}
