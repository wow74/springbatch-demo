package com.example.demo.validator;


import ch.qos.logback.core.util.StringUtil;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class RequiredValidator implements JobParametersValidator {

  @Override
  public void validate(JobParameters parameters) throws JobParametersInvalidException {

    String key = "require1";
    String require1 = parameters.getString(key);

    if (StringUtil.isNullOrEmpty(require1)) {
      String errorMsg = "Not enterd:" + key;
      throw new JobParametersInvalidException(errorMsg);

    }

  }
}
