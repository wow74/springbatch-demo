package com.example.demo.config;

import com.example.demo.listener.TaskletStepListener;
import com.example.demo.tasklet.FailTasklet;
import com.example.demo.tasklet.FirstTasklet;
import com.example.demo.tasklet.SuccessTasklet;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Autowired
  @Qualifier("FirstTasklet")
  private FirstTasklet firstTasklet;

  @Autowired
  @Qualifier("SuccessTasklet")
  private SuccessTasklet successTasklet;

  @Autowired
  @Qualifier("FailTasklet")
  private FailTasklet failTasklet;

  @Autowired
  @Qualifier("TaskletStepListener")
  private TaskletStepListener taskletStepListener;

  @Bean
  public Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("FirstStep", jobRepository)
            .tasklet(firstTasklet, transactionManager)
            .listener(taskletStepListener)
            .build();
  }

  @Bean
  public Step successStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("SuccessStep", jobRepository)
            .tasklet(successTasklet, transactionManager)
            .listener(taskletStepListener)
            .build();
  }

  @Bean
  public Step failStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("FailStep", jobRepository)
            .tasklet(failTasklet, transactionManager)
            .listener(taskletStepListener)
            .build();
  }

  @Bean
  public Job taskletBranchJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new JobBuilder("TaskletBranchJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(firstStep(jobRepository, transactionManager))
            .on(ExitStatus.COMPLETED.getExitCode())
            .to(successStep(jobRepository, transactionManager)) // completeの場合はsuccessStep()を実行
            .from(firstStep(jobRepository, transactionManager))
            .on(ExitStatus.FAILED.getExitCode())
            .to(failStep(jobRepository, transactionManager)) // failedの場合はfailStep()を実行
            .end()
            .build();
  }
}
