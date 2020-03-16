package com.vos.bootcamp.msclients.repositories;

import com.vos.bootcamp.msclients.models.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
