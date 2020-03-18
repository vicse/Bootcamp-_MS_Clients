package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.TypeCustomer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITypeCustomerService {

    public Flux<TypeCustomer> findAll();

    public Mono<TypeCustomer> findById(String id);

    public Mono<TypeCustomer> save(TypeCustomer TypeCustomer);

    public Mono<TypeCustomer> update(String id, TypeCustomer TypeCustomer);

    public Mono<Void> deleteById(String id);
    
}
