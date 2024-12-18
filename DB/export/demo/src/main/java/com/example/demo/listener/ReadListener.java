package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
public class ReadListener implements ItemReadListener<Employee> {

  @Override
  public void beforeRead() {

  }

  @Override
  public void afterRead(Employee item) {
    System.out.println("AfterRead: " + item);
  }

  @Override
  public void onReadError(Exception e) {
    System.out.println("ReadError: errorMsg " + e.getMessage() + ": " + e);
  }

}
