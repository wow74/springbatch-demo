package com.example.demo.validator;

import io.micrometer.common.util.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class OptionalValidator implements JobParametersValidator {

  @Override
  public void validate(JobParameters parameters) throws JobParametersInvalidException {
    String key = "option1";
    String option1 = parameters.getString(key);

    if (StringUtils.isEmpty(option1)) {
      return;
    }

    try {
      Integer.parseInt(option1);
    } catch (NumberFormatException e) {
      String errorMsg = "Not Number: value" + option1;
      throw new JobParametersInvalidException(errorMsg);
    }


  }
}
