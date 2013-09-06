package com.netapp.customers.server;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class CustomerRepositoryBean
        implements CustomerRepositoryLocalBusiness,
                   CustomerRepositoryRemoteBusiness {

  @PersistenceContext
  private EntityManager persistence;

  @Override
  public void add(Customer customer) {
    persistence.persist(customer);
  }

  @Override
  public void update(Customer customer) {
    persistence.merge(customer);
  }

  @Override
  public List<Customer> getCustomers() {
    CriteriaBuilder builder = persistence.getCriteriaBuilder();
    CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
    Root<Customer> customerRoot = criteria.from(Customer.class);
    criteria.select(customerRoot);
    criteria.orderBy(builder.asc(customerRoot.get(Customer_.customerId)));
    return persistence.createQuery(criteria).getResultList();
  }

  @Override
  public Customer getCustomer(Long id) {
    CriteriaBuilder builder = persistence.getCriteriaBuilder();
    CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
    Root<Customer> customerRoot = criteria.from(Customer.class);
    criteria.where(builder.equal(customerRoot.get(Customer_.customerId), id));
    criteria.select(customerRoot);
    criteria.orderBy(builder.asc(customerRoot.get(Customer_.customerId)));
    return persistence.createQuery(criteria).getSingleResult();
  }
}
