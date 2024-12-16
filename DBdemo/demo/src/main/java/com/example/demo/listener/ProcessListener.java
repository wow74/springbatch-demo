package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProcessListener implements ItemProcessListener<Employee, Employee> {

  @Override
  public void beforeProcess(Employee item) {

  }

  @Override
  public void afterProcess(Employee item, Employee result) {

  }

  @Override
  public void onProcessError(Employee item, Exception e) {
    log.error("ProcessError: item={}, errorMessage={}", item, e.getMessage(), e);
    System.out.println("ProcessError: item= " + item + "errorMessage= " +  e.getMessage() +  e);
  }

}
