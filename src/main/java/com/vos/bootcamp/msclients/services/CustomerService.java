package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    public Flux<Customer> findAll();

    public Mono<Customer> findById(String id);

    public Mono<Customer> create(Customer customer);

    public Mono<Void> delete(Customer customer);

}
