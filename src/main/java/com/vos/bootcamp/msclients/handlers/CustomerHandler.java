package com.vos.bootcamp.msclients.handlers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
public class CustomerHandler {

    @Autowired
    private ICustomerService service;

    @Qualifier("webFluxValidator")
    @Autowired
    private Validator validator;


    /* =====================================
        Function to List all customers
       ===================================== */

    public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Customer.class);
    }

    /* ===============================================
        Function to obtain a customer by id customer
       =============================================== */

    public Mono<ServerResponse> getByIdCustomer(ServerRequest request) {

        String id = request.pathVariable("id");
        return service.findById(id)
                .flatMap( p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p))
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }

    /* ===============================================
            Function to create a customer
    =============================================== */

    public Mono<ServerResponse> createCustomer(ServerRequest request) {

        Mono<Customer> Customer = request.bodyToMono(Customer.class);

        return Customer.flatMap(c -> {

            Errors errors = new BeanPropertyBindingResult(c, Customer.class.getName());
            validator.validate(c, errors);

            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(fieldError -> "The field " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> ServerResponse.badRequest().body(fromValue(list)));
            } else {
                return service.save(c).flatMap(customerDB -> ServerResponse.
                        created(URI.create("/api/customer/".concat(customerDB.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(customerDB)));
            }

        });

    }

    /* ===============================================
            Function to update a customer by id
    =============================================== */

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {

        Mono<Customer> customer = request.bodyToMono(Customer.class);
        String id = request.pathVariable("id");

        Mono<Customer> customerDB = service.findById(id);

        return customerDB.zipWith(customer, (db, req) -> {
            db.setNames(req.getNames());
            db.setSurnames(req.getSurnames());
            db.setPhoneNumber(req.getPhoneNumber());
            db.setAddress(req.getAddress());
            return db;
        }).flatMap(p -> ServerResponse.created(URI.create("/api/customer/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.save(p), Customer.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

     /* ===============================================
            Function to delete a customer by id
    =============================================== */

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {

        String id = request.pathVariable("id");
        Mono<Customer> customerDB = service.findById(id);

        return customerDB.flatMap(customer -> service.delete(customer)
                .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
