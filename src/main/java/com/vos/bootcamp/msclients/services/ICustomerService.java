package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

    public Flux<Customer> findAll();

    public Mono<Customer> findById(String id);

    public Mono<Customer> save(Customer customer);

    public Mono<Customer> update(String id, Customer customer);

    public Mono<Void> delete(Customer customer);

    public Mono<Void> deleteById(String id);

}
