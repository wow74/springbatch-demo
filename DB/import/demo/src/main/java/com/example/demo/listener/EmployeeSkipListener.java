package com.example.demo.listener;

import com.example.demo.domain.model.Employee;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EmployeeSkipListener implements SkipListener<Employee, Employee> {

  @Override
  public  void onSkipInRead(Throwable t) {
    System.out.println("Skip by Read Error: errorMsg= " + t.getMessage());
  }

  @Override
  public void onSkipInWrite(Employee item, Throwable t) {
    System.out.println("Skip by Write Error: item=" + item);
  }


  @Override
  public void onSkipInProcess(Employee item, Throwable t) {
    System.out.println("Skip by Process Error: item=" + item);
  }



}
