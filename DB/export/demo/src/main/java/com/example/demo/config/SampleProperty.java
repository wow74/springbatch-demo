package com.example.demo.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@PropertySource("classpath:property/sample.properties")
@Getter
@ToString
public class SampleProperty {

  @Value("${file.name}")
  private String fileName;

  @Value("${file.output.directory}")
  private String fileOutputDirectory;

  public String outputPath() {
    String outputPath = fileOutputDirectory + File.separator + fileName;
    System.out.println("outputPath=" + outputPath);
    return outputPath;
  }


}
