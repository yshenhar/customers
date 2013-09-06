package com.netapp.customers.rest;

import com.netapp.customers.server.Customer;
import com.netapp.customers.server.CustomerRepositoryCommonBusiness;

import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/customers")
public class CustomerResource {

  @Inject
  private CustomerRepositoryCommonBusiness customerRepo;

  @POST
  public void create(RestCustomer customer) {
    Customer persistedCustomer = new Customer(customer.firstName(), customer.lastName());
    customerRepo.add(persistedCustomer);
  }

  @PUT
  @Path("{id}")
  @Consumes("application/xml")
  public void updateCustomer(@PathParam("id") long id, RestCustomer customer) {
    Customer persistenceCustomer = new Customer(customer.firstName(), customer.lastName());
    persistenceCustomer.setCustomerId(id);
    customerRepo.update(persistenceCustomer);
  }
}
