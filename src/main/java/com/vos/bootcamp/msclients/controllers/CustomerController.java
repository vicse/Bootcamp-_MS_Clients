package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@Api(value = "Customer Microservice")
public class CustomerController {

  @Autowired
  private ICustomerService customerService;


  /* =====================================
    Function to List all customers
  ===================================== */
  @GetMapping
  @ApiOperation(value = "List all customers", notes = "List all customers of Collections")
  public Flux<Customer> getCustomers() {
    return customerService.findAll();
  }

  /* ===============================================
       Function to obtain a customer by id customer
  ============================================ */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a customer", notes = "Get a customer by Id")
  public Mono<ResponseEntity<Customer>> getByIdCustomer(@PathVariable String id) {
    return customerService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
    );
  }

  /* ===============================================
       Function to obtain a customer by Num Document
  ============================================ */
  @GetMapping("/param")
  @ApiOperation(value = "Get a customer by number Identity Document ", notes = "Get a customer by Id")
  public Mono<ResponseEntity<Customer>> getCustomerByNumDoc(@RequestParam String identityDoc) {
    return customerService.findByNumDoc(identityDoc)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
       Function to know if the customer exists
  ============================================ */
  @GetMapping("/{numDoc}/exist")
  @ApiOperation(value = "Customer exists", notes = "Validate if customer exists")
  public Mono<ResponseEntity<Boolean>> exitsCustomer(@PathVariable String numDoc) {
    return customerService.existsCustomer(numDoc)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
      Function to obtain a customerType by numDoc customer
 ============================================ */
  @GetMapping("/{numDoc}/customerType")
  @ApiOperation(value = "Get Customer Type", notes = "Validate if customer exists")
  public Mono<ResponseEntity<TypeCustomer>> getCustomerType(@PathVariable String numDoc) {
    return customerService.getTypeCustomer(numDoc)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
           Function to create a customer
  =============================================== */
  @PostMapping
  @ApiOperation(value = "Create customer", notes = "Create customer, check the model please")
  public Mono<ResponseEntity<Customer>> createCustomer(@Valid @RequestBody Customer customer) {
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
  @ApiOperation(value = "Update customer", notes = "Update customer by ID")
  public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable String id,
                                                       @RequestBody Customer customer) {
    return customerService.update(id, customer)
            .map(customerDB -> ResponseEntity
                    .created(URI.create("/api/customers/".concat(customerDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(customerDB))
            .defaultIfEmpty(ResponseEntity.notFound().build()
    );
  }

  /* ===============================================
           Function to delete a customer by id
  =============================================== */
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete Customer", notes = "Delete customer by ID")
  public Mono<ResponseEntity<Void>> deleteByIdCustomer(@PathVariable String id) {
    return customerService.deleteById(id)
            .map(res -> ResponseEntity
                    .ok()
                    .<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }


}
