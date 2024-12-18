package com.example.demo.processor;

import com.example.demo.domain.model.Employee;
import com.example.demo.repository.EmployeeJdbcRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ExistsCheckProcessor")
@StepScope
public class ExistsCheckProcessor implements ItemProcessor<Employee, Employee> {

  @Autowired
  private EmployeeJdbcRepository employeeJdbcRepository;

  @Override
  public Employee process(Employee item) throws Exception {
    boolean exists = employeeJdbcRepository.exists(item.getId());

    if (exists) {
      System.out.println("Skip key=" + item);
      return null; // nullを返すとwriterに渡されず該当データの処理がスキップされる
    }
    return item;
  }
}
