package com.example.demo.config;

import com.example.demo.domain.model.Employee;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
public class SkipImportBatchConfig extends BaseConfig {

  @Autowired
  private SkipListener<Employee, Employee> employeeSkipListener;

  @Autowired
  private MyBatisBatchItemWriter<Employee> myBatisBatchItemWriter;

  @Bean
  public Step csvImportSkipStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("CsvImportSkipStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(csvReader()).listener(readListener)
            .processor(this.genderConvertProcessor).listener(this.processListener)
            .writer(myBatisBatchItemWriter)
            .faultTolerant()
            .skipLimit(Integer.MAX_VALUE)
            .skip(RuntimeException.class)
            .listener(this.employeeSkipListener)
            .build();
  }

  @Bean("SkipJob")
  public Job csvImportSkipJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("CsvImportSkipJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(csvImportSkipStep(jobRepository, transactionManager))
            .build();
  }
}
