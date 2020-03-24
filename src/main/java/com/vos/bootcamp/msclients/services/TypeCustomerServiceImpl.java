package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.repositories.ITypeCustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeCustomerServiceImpl implements ITypeCustomerService {

    private final ITypeCustomerRepository repository;

    public TypeCustomerServiceImpl(ITypeCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<TypeCustomer> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<TypeCustomer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<TypeCustomer> save(TypeCustomer typeCustomer) {
        return repository.save(typeCustomer);
    }

    @Override
    public Mono<TypeCustomer> update(String id, TypeCustomer typeCustomer) {
        return repository.findById(id).flatMap( typeCustomerDB -> {

            if (typeCustomer.getName() == null) typeCustomerDB.setName(typeCustomerDB.getName());
            else typeCustomerDB.setName(typeCustomer.getName());

            return repository.save(typeCustomerDB);
        });
    }

    @Override
    public Mono<TypeCustomer> deleteById(String id) {
        return repository.findById(id)
                .flatMap(typeCustomer -> repository.delete(typeCustomer)
                .then(Mono.just(typeCustomer))
                );
    }
}

