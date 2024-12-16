package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReadListener implements ItemReadListener<Employee> {

  @Override
  public void beforeRead() {

  }

  @Override
  public void afterRead(Employee item) {
    log.debug("AfterRead: {}", item);
    System.out.println("AfterRead: " + item);
  }

  @Override
  public void onReadError(Exception ex) {
    log.error("ReadError: errorMessage", ex.getMessage(), ex);
    System.out.println("ReadError: errorMessage" + ex.getMessage() + ex);
  }
}
