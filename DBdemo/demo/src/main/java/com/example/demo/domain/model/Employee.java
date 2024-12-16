package com.example.demo.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Employee {
  @NotNull
  private Integer id;

  @NotNull
  private String name;

  @Min(20)
  private Integer age;
  private Integer gender;
  private String genderString;

  public void convertGenderStringToInt() {
    if ("男性".equals(genderString)) {
      gender = 1;
    } else if ("女性".equals(genderString)) {
      gender = 2;
    } else {
      String errorMsg = "Gender string is invalid:" + genderString;
      throw new IllegalStateException(errorMsg);
    }
  }
}
