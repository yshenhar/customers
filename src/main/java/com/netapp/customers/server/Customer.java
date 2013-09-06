package com.netapp.customers.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Customer {

  private Long customerId;
  private String firstName;
  private String lastName;

  public Customer(){}
  public Customer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Id @GeneratedValue
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long id) {
    this.customerId = id;
  }

  @NotNull
  @Size(min=3, max=50)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String first) {
    this.firstName = first;
  }

  @NotNull
  @Size(min=3, max=50)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String last) {
    this.lastName = last;
  }
}
