package com.example.demo.config;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import java.nio.charset.StandardCharsets;

public abstract class BaseConfig {

  @Autowired
  protected ItemProcessor<Employee, Employee> genderConvertProcessor;

  @Autowired
  protected SampleProperty property;

  @Autowired
  protected ItemReadListener<Employee> readListener;

  @Autowired
  protected ItemWriteListener<Employee> writeListener;

  @Autowired
  protected FlatFileHeaderCallback csvHeaderCallback;

  @Autowired
  protected FlatFileFooterCallback csvFooterCallback;

  @Bean
  @StepScope
  public FlatFileItemWriter<Employee> csvWriter() {
    String filePath = property.outputPath();
    WritableResource outputResource = new FileSystemResource(filePath);

    DelimitedLineAggregator<Employee> aggregator = new DelimitedLineAggregator<>();
    aggregator.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);

    BeanWrapperFieldExtractor<Employee> extractor = new BeanWrapperFieldExtractor<>();
    extractor.setNames(new String[]{"id", "name", "age", "genderString"});
    aggregator.setFieldExtractor(extractor);

    return new FlatFileItemWriterBuilder<Employee>()
            .name("employeeCsvWriter")
            .resource(outputResource)
            .append(false)
            .lineAggregator(aggregator)
            .headerCallback(csvHeaderCallback)
            .footerCallback(csvFooterCallback)
            .encoding(StandardCharsets.UTF_8.name())
            .build();
  }
}
