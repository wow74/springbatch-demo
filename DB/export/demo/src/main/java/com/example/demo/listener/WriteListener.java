package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriteListener implements ItemWriteListener<Employee> {

  public void beforeWrite(List<? extends Employee> items) {

  }

  public void afterWrite(List<? extends Employee> items) {
    System.out.println("AfterWrite: count= " + items.size());
  }

  public void onWriteError(Exception e, List<? extends Employee> items) {
    System.out.println("WriteError: errorMsg=" + e.getMessage() + ": " + e);
  }
}
