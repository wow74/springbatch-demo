package com.example.demo.config;

import com.example.demo.domain.model.Employee;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
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
public class MyBatisImportBatchConfig extends BaseConfig {

  @Autowired
  private SqlSessionFactory sqlSessionFactory;

  @Bean
  public MyBatisBatchItemWriter<Employee> mybatisWriter() {
    return new MyBatisBatchItemWriterBuilder<Employee>()
            .sqlSessionFactory(sqlSessionFactory)
            .statementId("com.example.demo.repository.EmployeeMapper.insertOne")
            .build();
  }

  @Bean
  public Step csvImportMybatisStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("CsvImportMybatisStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(csvReader()).listener(this.readListener)
            .processor(compositeProcessor()).listener(this.processListener)
            .writer(mybatisWriter()).listener(this.writeListener)
            .build();
  }

  @Bean("MybatisJob")
  public Job csvImportMybatisJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("CsvImportMybatisJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(csvImportMybatisStep(jobRepository, transactionManager))
            .build();
  }
}
