package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WriteListener implements ItemWriteListener<Employee> {

  public void beforeWrite(List<? extends Employee> items) {

  }

  public void afterWrite(List<? extends Employee> items) {
    log.debug("AfterWrite: count={}", items.size());
    System.out.println("AfterWrite: count= " + items.size());
  }

  public void onWriteError(Exception exception, List<? extends Employee> items) {
    log.error("WriteError: errorMessage={}", exception.getMessage(), exception);
    System.out.println("WriteError: errorMessage=" + exception.getMessage() + exception);
  }

}
