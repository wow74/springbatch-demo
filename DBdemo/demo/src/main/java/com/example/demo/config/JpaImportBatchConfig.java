package com.example.demo.config;

import com.example.demo.domain.model.Employee;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JpaImportBatchConfig extends BaseConfig {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Bean
  public JpaItemWriter<Employee> jpaWriter() {
    JpaItemWriter<Employee> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(this.entityManagerFactory);
    return writer;
  }

  @Bean
  public Step csvImportJpaStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("CsvImportJpaStep", jobRepository)
            .<Employee, Employee>chunk(10, transactionManager)
            .reader(csvReader()).listener(this.readListener)
            .processor(compositeProcessor()).listener(this.processListener)
            .writer(jpaWriter()).listener(this.writeListener)
            .build();
  }

  @Bean
  public Job csvImportJpaJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("CsvImportJpaJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(csvImportJpaStep(jobRepository, transactionManager))
            .build();
  }
}
