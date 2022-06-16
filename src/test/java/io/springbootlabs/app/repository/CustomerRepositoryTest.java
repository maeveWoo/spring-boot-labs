package io.springbootlabs.app.repository;

import io.springbootlabs.domain.user.Customer;
import io.springbootlabs.domain.user.CustomerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void select() {
        List<Customer> name = customerRepository.findByName("Neil Ducich");
        assertThat(name).isNotNull();
    }

    @Test
    public void save() {
        String test = "TEST";
        Customer expect = new Customer(test, test, CustomerType.Consumer);
        Customer actual = customerRepository.save(expect);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(test),
                () -> assertThat(actual.getName()).isEqualTo(test)
        );
    }
}