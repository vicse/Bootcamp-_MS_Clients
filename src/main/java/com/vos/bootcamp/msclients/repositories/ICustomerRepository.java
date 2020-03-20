package com.vos.bootcamp.msclients.repositories;

import com.vos.bootcamp.msclients.models.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ICustomerRepository extends ReactiveMongoRepository<Customer, String> {

  public Mono<Customer> findByNumIdentityDoc(String numDoc);

  public Mono<Boolean> existsByNumIdentityDoc(String numDoc);

}
