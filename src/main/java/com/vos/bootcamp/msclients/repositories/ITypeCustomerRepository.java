package com.vos.bootcamp.msclients.repositories;

import com.vos.bootcamp.msclients.models.TypeCustomer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITypeCustomerRepository extends ReactiveMongoRepository<TypeCustomer, String> {
}
