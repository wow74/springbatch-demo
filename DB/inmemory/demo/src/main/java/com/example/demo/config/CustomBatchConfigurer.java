package com.example.demo.config;

import lombok.Getter;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CustomBatchConfigurer extends DefaultBatchConfiguration {

  @Autowired
  private DataSource batchDataSource;

  @Override
  protected DataSource getDataSource() {
    return batchDataSource;
  }

  @Override
  public PlatformTransactionManager getTransactionManager() {
    return new ResourcelessTransactionManager();
  }
}
