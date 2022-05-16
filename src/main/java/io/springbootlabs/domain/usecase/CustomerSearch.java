package io.springbootlabs.domain.usecase;

import io.springbootlabs.domain.entity.Customer;
import io.springbootlabs.domain.entity.CustomerType;

import java.util.List;

public interface CustomerSearch {
    public Customer findCustomer(String id);
    public List<Customer> findCustomers(CustomerType type);
    public List<Customer> findCustomers(Integer postcode);

    public List<Customer> findCustomers(String name);


}
