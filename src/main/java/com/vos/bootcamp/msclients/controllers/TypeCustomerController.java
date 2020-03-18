package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ITypeCustomerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/typesCustomer")
@Api(value="Customer Microservice")
public class TypeCustomerController {

    @Autowired
    private ITypeCustomerService typeCustomerService;

    /* =====================================
        Function to List all typesCustomer
       ===================================== */

    @GetMapping
    public Mono<ResponseEntity<Flux<TypeCustomer>>> getTypesCustomer() {
        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(typeCustomerService.findAll())
        );
    }

    /* ===============================================
        Function to obtain a typeCustomer by id typeCustomer
       ============================================ */

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TypeCustomer>> getByIdTypeCustomer(@PathVariable String id) {
        return typeCustomerService.findById(id)
                .map(typeCustomer -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(typeCustomer)
                )
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );
    }

    /* ===============================================
            Function to create a typeCustomer
    =============================================== */

    @PostMapping
    public Mono<ResponseEntity<TypeCustomer>> createTypeCustomer(@Valid @RequestBody TypeCustomer typeCustomer){
        return typeCustomerService.save(typeCustomer)
                .map(typeCustomerDB -> ResponseEntity
                        .created(URI.create("/api/typesCustomer/".concat(typeCustomerDB.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(typeCustomerDB)
                );
    }

    /* ===============================================
            Function to update a typeCustomer by id
    =============================================== */

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TypeCustomer>> updateTypeCustomer(@PathVariable String id, @RequestBody TypeCustomer typeCustomer) {
        return typeCustomerService.update(id, typeCustomer)
                .map(typeCustomerDB -> ResponseEntity
                        .created(URI.create("/api/typesCustomer/".concat(typeCustomerDB.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(typeCustomerDB))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

     /* ===============================================
            Function to delete a typeCustomer by id
    =============================================== */

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteByIdTypeCustomer(@PathVariable String id) {
        return typeCustomerService.deleteById(id)
                .map(typeCustomerDeleted -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(typeCustomerDeleted)
                )
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );

    }




}
