package com.example.demo.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:property/sample.properties")
@Getter
@ToString
public class SampleProperty {

  @Value("${csv.path}")
  private String csvPath;


}
