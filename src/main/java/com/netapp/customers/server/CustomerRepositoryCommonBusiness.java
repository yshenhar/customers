package com.netapp.customers.server;

import java.util.List;

public interface CustomerRepositoryCommonBusiness {
  void add(Customer customer);
  void update(Customer customer);
  List<Customer> getCustomers();
  Customer getCustomer(Long id);
}
