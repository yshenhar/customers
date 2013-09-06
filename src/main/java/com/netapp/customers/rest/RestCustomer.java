package com.netapp.customers.rest;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestCustomer {

  @XmlAttribute
  private Long customerId;

  @XmlElement
  private String firstName;

  @XmlElement
  private String lastName;

  public RestCustomer() {}
  public RestCustomer(Long customerId, String firstName, String lastName) {
    this.customerId = customerId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Long id() {
    return customerId;
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }
}
