package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.services.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value="Customer Microservice")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;


    /* =====================================
        Function to List all customers
       ===================================== */

    @GetMapping
    @ApiOperation(value = "List all customers", notes="List all customers of Collections")
    public Mono<ResponseEntity<Flux<Customer>>> getCustomers() {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.findAll())
        );
    }

    /* ===============================================
        Function to obtain a customer by id customer
       ============================================ */

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a customer", notes="Get a customer by Id")
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
    @ApiOperation(value = "Create customer", notes="Create customer, check the model please")
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
    @ApiOperation(value = "Update customer", notes="Update customer by ID")
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
    @ApiOperation(value = "Delete Customer", notes="Delete customer by ID")
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
