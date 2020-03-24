package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ITypeCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CustomerTypeControllerTest {


  @Mock
  private ITypeCustomerService typeCustomerService;
  private WebTestClient client;
  private List<TypeCustomer> expectedCustomersTypes;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new TypeCustomerController(typeCustomerService))
            .configureClient()
            .baseUrl("/api/typesCustomer")
            .build();

    expectedCustomersTypes = Arrays.asList(
            TypeCustomer.builder().id("1").name("CustomerType name 1 test").build(),
            TypeCustomer.builder().id("2").name("CustomerType name 2 test").build(),
            TypeCustomer.builder().id("3").name("CustomerType name 3 test").build());

  }

  @Test
  void getAllProducts() {
    when(typeCustomerService.findAll()).thenReturn(Flux.fromIterable(expectedCustomersTypes));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(TypeCustomer.class)
            .isEqualTo(expectedCustomersTypes);
  }
  @Test
  void getCustomerTypeById_whenCustomerTypeExists_returnCorrectCustomerType() {
    TypeCustomer expectedCustomertype = expectedCustomersTypes.get(0);
    when(typeCustomerService.findById(expectedCustomertype.getId())).thenReturn(Mono.just(expectedCustomertype));

    client.get()
            .uri("/{id}", expectedCustomertype.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(TypeCustomer.class)
            .isEqualTo(expectedCustomertype);
  }

  @Test
  void getCustomerTypeById_whenCustomerTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(typeCustomerService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addCustomerType() {
    TypeCustomer expectedCustomerType = expectedCustomersTypes.get(0);
    when(typeCustomerService.save(expectedCustomerType)).thenReturn(Mono.just(expectedCustomerType));

    client.post()
            .uri("/")
            .body(Mono.just(expectedCustomerType), TypeCustomer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(TypeCustomer.class)
            .isEqualTo(expectedCustomerType);
  }

  @Test
  void updateCustomerType_whenCustomerTypeExists_performUpdate() {
    TypeCustomer expectedCustomerType = expectedCustomersTypes.get(0);
    when(typeCustomerService.update(expectedCustomerType.getId(), expectedCustomerType)).thenReturn(Mono.just(expectedCustomerType));

    client.put()
            .uri("/{id}", expectedCustomerType.getId())
            .body(Mono.just(expectedCustomerType), TypeCustomer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(TypeCustomer.class)
            .isEqualTo(expectedCustomerType);
  }

  @Test
  void updateCustomerType_whenCustomerTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    TypeCustomer expectedCustomerType = expectedCustomersTypes.get(0);
    when(typeCustomerService.update(id, expectedCustomerType)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedCustomerType), TypeCustomer.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteCustomerType_whenCustomerTypeExists_performDeletion() {
    TypeCustomer customerTypeToDelete = expectedCustomersTypes.get(0);
    when(typeCustomerService.deleteById(customerTypeToDelete.getId())).thenReturn(Mono.just(customerTypeToDelete));

    client.delete()
            .uri("/{id}", customerTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteCustomerType_whenIdNotExist_returnNotFound() {
    TypeCustomer customerTypeToDelete = expectedCustomersTypes.get(0);
    when(typeCustomerService.deleteById(customerTypeToDelete.getId())).thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", customerTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }


}
