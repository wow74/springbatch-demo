package com.example.demo.config;

import com.example.demo.tasklet.HelloTasklet;
import com.example.demo.tasklet.HelloTasklet2;
import com.example.demo.validator.OptionalValidator;
import com.example.demo.validator.RequiredValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatchConfig {

  @Autowired
  @Qualifier("HelloTasklet")
  private HelloTasklet helloTasklet;

  @Autowired
  @Qualifier("HelloTasklet2")
  private HelloTasklet2 helloTasklet2;

  @Bean
  // パラメータ名のみ必須チェック(値が入っているかはみない)
  public JobParametersValidator defaultValidator() {
    DefaultJobParametersValidator validator = new DefaultJobParametersValidator();

    // 必須
    String[] requiredKey = new String[]{"run.id", "require1"};
    validator.setRequiredKeys(requiredKey);

    // 任意
    String[] optionalKeys = new String[]{"option1"};
    validator.setOptionalKeys(optionalKeys);

    // 必須と任意に重複がないことを確認
    validator.afterPropertiesSet();
    return validator;

  }

  @Bean
  // 値チェックも行う
  public JobParametersValidator compositeValidator() {
    List<JobParametersValidator> validators = new ArrayList<>();
    validators.add(defaultValidator());
    validators.add(new RequiredValidator());
    validators.add(new OptionalValidator());

    CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();
    compositeValidator.setValidators(validators);
    return compositeValidator;

  }

  // spring3系
  @Bean
  public Step sampleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("HelloStep", jobRepository)
            .tasklet(helloTasklet, transactionManager)
            .build(); // Stepの生成
  }

  @Bean
  public Step sampleStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("HelloStep2", jobRepository)
            .tasklet(helloTasklet2, transactionManager)
            .build();
  }

  // spring3系
  @Bean
  public Job sampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("HelloJob", jobRepository)
            .incrementer(new RunIdIncrementer()) // IDのインクリメント
            .start(sampleStep(jobRepository, transactionManager)) // 最初のstep
            .next(sampleStep2(jobRepository, transactionManager))
//            .validator(defaultValidator())
            .validator(compositeValidator())
            .build();  // Jobの生成
  }
}
