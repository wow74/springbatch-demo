package com.example.demo.processor;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("GenderConvertProcessor")
@StepScope
public class GenderConvertProcessor implements ItemProcessor<Employee, Employee> {

  @Override
  public Employee process(Employee item) throws Exception {
    try {
      item.convertGenderIntToString();
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage() + ": " + e);
      return null;
    }
    return item;
  }
}
