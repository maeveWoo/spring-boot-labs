package io.springbootlabs.app.repository;

import io.springbootlabs.domain.user.Customer;
import io.springbootlabs.domain.user.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByName(String name);
    List<Customer> findByType(CustomerType type);

    Optional<Customer> findById(String id);
}
