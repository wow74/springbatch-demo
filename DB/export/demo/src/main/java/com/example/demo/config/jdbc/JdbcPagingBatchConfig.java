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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JdbcPagingBatchConfig extends BaseConfig {

  @Autowired
  private DataSource dataSource;

  @Bean
  public SqlPagingQueryProviderFactoryBean queryProvider() {

    SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
    provider.setDataSource(dataSource);
    provider.setSelectClause("select id, name, age, gender");
    provider.setFromClause("from employee");
    provider.setWhereClause("where gender = :genderParam");
    provider.setSortKey("id");
    return provider;
  }

  @Bean
  @StepScope
  public JdbcPagingItemReader<Employee> jdbcPagingReader() throws Exception {
    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("genderParam", 1);
    RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
    return new JdbcPagingItemReaderBuilder<Employee>()
            .name("jdbcPagingItemReader")
            .dataSource(dataSource)
            .queryProvider(queryProvider().getObject())
            .parameterValues(parameterValues)
            .rowMapper(rowMapper)
            .pageSize(5)
            .build();
  }

  @Bean
  public Step exportJdbcPagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new StepBuilder("ExportJdbcPagingStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(jdbcPagingReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
  }

  @Bean("JdbcPagingJob")
  public Job exportJdbcPagingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("ExportJdbcPagingJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(exportJdbcPagingStep(jobRepository, transactionManager))
            .build();
  }
}
