package com.example.demo.config;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BaseConfig {

  @Autowired
  @Qualifier("GenderConvertProcessor")
  protected ItemProcessor<Employee, Employee> genderConvertProcessor;

  @Autowired
  protected ItemReadListener<Employee> readListener;

  @Autowired
  protected ItemProcessListener<Employee, Employee> processListener;

  @Autowired
  protected ItemWriteListener<Employee> writeListener;

  @Autowired
  protected SampleProperty sampleProperty;

  @Autowired
  @Qualifier("ExistsCheckProcessor")
  protected ItemProcessor<Employee, Employee> existsCheckProcessor;

  @Bean
  @StepScope
  public FlatFileItemReader<Employee> csvReader() {
    String[] nameArray = new String[]{"id", "name", "age", "genderString"};

    return new FlatFileItemReaderBuilder<Employee>()
            .name("employeeCsvReader")
            .resource(new ClassPathResource(sampleProperty.getCsvPath()))
            .linesToSkip(1)
            .encoding(StandardCharsets.UTF_8.name())
            .delimited()
            .names(nameArray) // カラム名
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
              {
                setTargetType(Employee.class); // データを指定のクラスにバインド
              }
            })
            .build();

  }

  @Bean
  @StepScope
  public ItemProcessor<Employee, Employee> compositeProcessor() {
    CompositeItemProcessor<Employee, Employee> compositeItemProcessor = new CompositeItemProcessor<>();
    compositeItemProcessor.setDelegates(Arrays.asList(validationProcessor(), this.existsCheckProcessor, this.genderConvertProcessor)); // 実行したい順に格納
    return compositeItemProcessor;
  }

  @Bean
  @StepScope
  public BeanValidatingItemProcessor<Employee> validationProcessor() {
    BeanValidatingItemProcessor<Employee> validatingItemProcessor = new BeanValidatingItemProcessor<>();
    validatingItemProcessor.setFilter(true); // true:skip, false:スロー
    return validatingItemProcessor;
  }
}
