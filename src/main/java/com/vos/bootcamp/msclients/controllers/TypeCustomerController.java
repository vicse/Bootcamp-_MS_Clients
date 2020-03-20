package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ITypeCustomerService;
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
@RequestMapping("/api/typesCustomer")
@Api(value="Customer Microservice")
public class TypeCustomerController {

    @Autowired
    private ITypeCustomerService typeCustomerService;

    /* =====================================
        Function to List all typesCustomer
       ===================================== */

    @GetMapping
    @ApiOperation(value = "List all Customer Types", notes="List all customer types of Collections")
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
    @ApiOperation(value = "Get a Customer Type", notes="Get a customer type by Id")
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
    @ApiOperation(value = "Create customer Type", notes="Create customer type, check the model please")
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
    @ApiOperation(value = "Update customer Type", notes="Update customer type by ID")
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
    @ApiOperation(value = "Delete Customer Type", notes="Delete customer type by ID")
    public Mono<ResponseEntity<Object>> deleteByIdTypeCustomer(@PathVariable String id) {
        return typeCustomerService.deleteById(id)
                .then(Mono.just(ResponseEntity
                        .noContent()
                        .build()
                )).defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );

    }




}
