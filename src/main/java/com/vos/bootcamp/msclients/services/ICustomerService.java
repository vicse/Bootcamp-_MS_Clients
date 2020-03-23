package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

    public Flux<Customer> findAll();

    public Mono<Customer> findById(String id);

    public Mono<Customer> save(Customer customer);

    public Mono<Customer> update(String id, Customer customer);

    public Mono<Customer> deleteById(String id);

    public Mono<Customer> findByNumDoc(String numDoc);

    public Mono<Boolean> existsCustomer(String numDoc);

    public Mono<TypeCustomer> getTypeCustomer(String numDoc);

}
