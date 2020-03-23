package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.repositories.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository repository;

    public CustomerServiceImpl(ICustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Customer> save(Customer customer)  {
        return repository.save(customer);
    }

    @Override
    public Mono<Customer> update(String id, Customer customer) {
        return repository.findById(id).flatMap( customerDB -> {

            if (customer.getNames() == null) customerDB.setNames(customerDB.getNames());
            else customerDB.setNames(customer.getNames());

            if (customer.getSurnames() == null) customerDB.setSurnames(customerDB.getSurnames());
            else customerDB.setSurnames(customer.getSurnames());

            if (customer.getNumIdentityDoc() == null) customerDB.setNumIdentityDoc(customerDB.getNumIdentityDoc());
            else customerDB.setNumIdentityDoc(customer.getNumIdentityDoc());

            if (customer.getEmail() == null) customerDB.setEmail(customerDB.getEmail());
            else customerDB.setEmail(customer.getEmail());

            if (customer.getPhoneNumber() == null) customerDB.setPhoneNumber(customerDB.getPhoneNumber());
            else customerDB.setPhoneNumber(customer.getPhoneNumber());


            if (customer.getAddress() == null) customerDB.setAddress(customerDB.getAddress());
            else customerDB.setAddress(customer.getAddress());

            if (customer.getTypeCustomer() == null) customerDB.setTypeCustomer(customerDB.getTypeCustomer());
            else customerDB.setTypeCustomer(customer.getTypeCustomer());

            return repository.save(customerDB);
        });
    }

    @Override
    public Mono<Customer> deleteById(String id) {
        return  repository.findById(id)
                .flatMap( customerDB -> repository.delete(customerDB)
                        .then(Mono.just(customerDB))
                );
    }

    @Override
    public Mono<Customer> findByNumDoc(String numDoc) {
        return repository.findByNumIdentityDoc(numDoc);
    }

    @Override
    public Mono<Boolean> existsCustomer(String numDoc) {
        return repository.existsByNumIdentityDoc(numDoc);
    }

    @Override
    public Mono<TypeCustomer> getTypeCustomer(String numDoc) {
        return repository.findByNumIdentityDoc(numDoc).flatMap(customer -> Mono.just(customer.getTypeCustomer()));
    }
}
