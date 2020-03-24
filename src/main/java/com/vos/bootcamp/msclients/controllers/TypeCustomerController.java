package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ITypeCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/typesCustomer")
@Api(value = "Customer Microservice")
public class TypeCustomerController {

  private final ITypeCustomerService typeCustomerService;

  public TypeCustomerController(ITypeCustomerService typeCustomerService) {
    this.typeCustomerService = typeCustomerService;
  }

  /* =====================================
      Function to List all typesCustomer
     ===================================== */

  @GetMapping
  @ApiOperation(value = "List all Customer Types", notes = "List all customer types of Collections")
  public Flux<TypeCustomer> getTypesCustomer() {
    return typeCustomerService.findAll();
  }

  /* ===============================================
      Function to obtain a typeCustomer by id typeCustomer
     ============================================ */

  @GetMapping("/{id}")
  @ApiOperation(value = "Get a Customer Type", notes = "Get a customer type by Id")
  public Mono<ResponseEntity<TypeCustomer>> getByIdTypeCustomer(@PathVariable String id) {
    return typeCustomerService.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
          Function to create a typeCustomer
  =============================================== */

  @PostMapping
  @ApiOperation(value = "Create customer Type", notes = "Create customer type, check the model please")
  public Mono<ResponseEntity<TypeCustomer>> createTypeCustomer(@Valid @RequestBody TypeCustomer typeCustomer) {
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
  @ApiOperation(value = "Update customer Type", notes = "Update customer type by ID")
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
  @ApiOperation(value = "Delete Customer Type", notes = "Delete customer type by ID")
  public Mono<ResponseEntity<Void>> deleteByIdTypeCustomer(@PathVariable String id) {
    return typeCustomerService.deleteById(id)
                .map(res -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity
                        .notFound()
                        .build()
                );

  }




}
