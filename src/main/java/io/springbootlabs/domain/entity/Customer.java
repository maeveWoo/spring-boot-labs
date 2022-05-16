package io.springbootlabs.domain.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
public class Customer {
    @Id
    @Column(name = "customer_id", nullable = false)
    private String id;

    @Column(name = "customer_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType type;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postcode")
    private Integer postCode;

    @Column(name = "region_type")
    private String regionType;

    protected Customer() {}

    public Customer(String id, String name, CustomerType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
