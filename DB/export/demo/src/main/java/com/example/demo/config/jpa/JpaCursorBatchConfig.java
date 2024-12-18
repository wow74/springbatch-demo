package com.example.demo.config.jpa;

import com.example.demo.config.BaseConfig;
import com.example.demo.domain.model.Employee;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JpaCursorBatchConfig extends BaseConfig {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Bean
  @StepScope
  public JpaCursorItemReader<Employee> jpaCursorReader() {
    String sql = "select * from employee where gender = :genderParam";
    JpaNativeQueryProvider<Employee> queryProvider = new JpaNativeQueryProvider<>();
    queryProvider.setSqlQuery(sql);
    queryProvider.setEntityClass(Employee.class);

    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("genderParam", 1);
    return new JpaCursorItemReaderBuilder<Employee>()
            .entityManagerFactory(entityManagerFactory)
            .name("jpaCursorItemReader")
            .queryProvider(queryProvider)
            .parameterValues(parameterValues)
            .build();
  }

  @Bean
  public Step exportJpaCursorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new StepBuilder("ExportJpaCursorStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(jpaCursorReader()).listener(readListener)
            .processor(this.genderConvertProcessor)
            .writer(csvWriter()).listener(writeListener)
            .build();
  }

  @Bean("JpaCursorJob")
  public Job exportJpaCursorJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("ExportJpaCursorJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(exportJpaCursorStep(jobRepository, transactionManager))
            .build();
  }
}
