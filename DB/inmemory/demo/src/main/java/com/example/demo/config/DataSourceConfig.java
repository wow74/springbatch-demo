package com.example.demo.config;

import com.example.demo.property.BatchDataSourceProperties;
import com.example.demo.property.MysqlDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

  @Autowired
  private BatchDataSourceProperties batchDataSourceProperties;

  @Autowired
  private MysqlDataSourceProperties mysqlDataSourceProperties;

  @Bean
  public DataSource batchDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(batchDataSourceProperties.getDriverClassName());
    dataSource.setUrl(batchDataSourceProperties.getUrl());
    dataSource.setUsername(batchDataSourceProperties.getUsername());
    dataSource.setPassword(batchDataSourceProperties.getPassword());
    return dataSource;
  }

  @Bean
  public DataSource mysqlDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(mysqlDataSourceProperties.getDriverClassName());
    dataSource.setUrl(mysqlDataSourceProperties.getUrl());
    dataSource.setUsername(mysqlDataSourceProperties.getUsername());
    dataSource.setPassword(mysqlDataSourceProperties.getPassword());
    return dataSource;
  }
}
