package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;


    /* =====================================
        Function to List all customers
       ===================================== */

    @GetMapping
    public Mono<ResponseEntity<Flux<Customer>>> getCustomers() {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.findAll())
        );
    }

       /* ===============================================
        Function to obtain a customer by id customer
       =============================================== */

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getByIdCustomer(@PathVariable String id) {
        return customerService.findById(id)
                .map(customer -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(customer)
                )
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );
    }

    /* ===============================================
            Function to create a customer
    =============================================== */

    @PostMapping
    public Mono<ResponseEntity<Customer>> createCustomer(@Valid @RequestBody Customer customer){
        return customerService.save(customer)
                    .map(customerDB -> ResponseEntity
                            .created(URI.create("/api/customers/".concat(customerDB.getId())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(customerDB)
                    );
    }


     /* ===============================================
            Function to update a customer by id
    =============================================== */

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        return customerService.update(id, customer)
                .map(customerDB -> ResponseEntity
                        .created(URI.create("/api/customers".concat(customerDB.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(customerDB))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

      /* ===============================================
            Function to delete a customer by id
    =============================================== */

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteByIdCustomer(@PathVariable String id) {
        return customerService.deleteById(id)
                .map(customer -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(customer)
                )
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );

    }


}
