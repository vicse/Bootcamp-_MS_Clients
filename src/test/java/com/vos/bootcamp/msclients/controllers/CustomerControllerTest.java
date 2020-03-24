package com.vos.bootcamp.msclients.controllers;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.services.ICustomerService;
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
public class CustomerControllerTest {

  private final TypeCustomer typeCustomer1 = TypeCustomer.builder().name("Type of customer 1").build();
  private final TypeCustomer typeCustomer2 = TypeCustomer.builder().name("Type of customer 2").build();

  @Mock
  private ICustomerService customerService;
  private WebTestClient client;
  private List<Customer> expectedCustomers;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new CustomerController(customerService))
            .configureClient()
            .baseUrl("/api/customers")
            .build();

    expectedCustomers = Arrays.asList(
            Customer.builder().id("1").names("Vicse").surnames("Ore Soto").numIdentityDoc("75772936")
                    .email("vicseore@gmail.com").phoneNumber("945026794").address("Calle 1 El Agustino")
                    .typeCustomer(typeCustomer1).build(),
            Customer.builder().id("2").names("Cristian").surnames("Huaynates Soto").numIdentityDoc("34256278")
                    .email("cheles@gmail.com").phoneNumber("990123568").address("Los frailes 401")
                    .typeCustomer(typeCustomer2).build(),
            Customer.builder().id("3").names("Carlos").surnames("Huaynates Soto").numIdentityDoc("70347856")
                    .email("galdoz@gmail.com").phoneNumber("993412354").address("Los frailes 401")
                    .typeCustomer(typeCustomer2).build());

  }

  @Test
  void getAllProducts() {
    when(customerService.findAll()).thenReturn(Flux.fromIterable(expectedCustomers));

    client.get().uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Customer.class)
            .isEqualTo(expectedCustomers);
  }

  @Test
  void getCustomerById_whenCustomerExists_returnCorrectCustomer() {
    Customer expectedCustomer = expectedCustomers.get(0);
    when(customerService.findById(expectedCustomer.getId())).thenReturn(Mono.just(expectedCustomer));

    client.get().uri("/{id}", expectedCustomer.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer);
  }

  @Test
  void getCustomerById_whenCustomerNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(customerService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void getCustomerTypeById_whenCustomerExists_returnCorrectCustomer() {
    Customer expectedCustomer = expectedCustomers.get(0);
    when(customerService.getTypeCustomer(expectedCustomer.getNumIdentityDoc())).thenReturn(Mono.just(expectedCustomer.getTypeCustomer()));

    client.get()
            .uri("/{numDoc}/customerType", expectedCustomer.getNumIdentityDoc())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(TypeCustomer.class)
            .isEqualTo(expectedCustomer.getTypeCustomer());
  }

  @Test
  void getCustomerTypeById_whenCustomerNotExist_returnNotFound() {
    String numDoc = "NOT_EXIST_ID";
    when(customerService.getTypeCustomer(numDoc)).thenReturn(Mono.empty());

    client.get()
            .uri("/{numDoc}/customerType", numDoc)
            .exchange()
            .expectStatus()
            .isNotFound();
  }



  @Test
  void searchByNumDocIdentity_whenCustomerExists_returnCorrectCustomer() {
    String numDoc = "75772936";
    Customer expectedCustomer = expectedCustomers.get(0);
    when(customerService.findByNumDoc(numDoc)).thenReturn(Mono.just(expectedCustomer));

    client.get()
            .uri("/param/identityDoc/{numDoc}", numDoc)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer);
  }

  @Test
  void searchByNumDocIdentity_whenCustomerNotExist_returnNotFound() {
    String numDoc = "NOT_EXIST_ID";
    when(customerService.findByNumDoc(numDoc)).thenReturn(Mono.empty());

    client.get()
            .uri("/param/identityDoc/{numDoc}", numDoc)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void validateIfCustomerExists() {
    String numDoc = "75772936";
    when(customerService.existsCustomer(numDoc)).thenReturn(Mono.just(true));

    client.get()
            .uri("/{numDoc}/exist", numDoc)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(true);
  }

  @Test
  void validateIfCustomerNotExists() {
    String numDoc = "NOT_EXIST_ID";
    when(customerService.existsCustomer(numDoc)).thenReturn(Mono.just(false));

    client.get()
            .uri("/{numDoc}/exist", numDoc)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(false);
  }

  @Test
  void addCustomer() {
    Customer expectedCustomer = expectedCustomers.get(0);
    when(customerService.save(expectedCustomer)).thenReturn(Mono.just(expectedCustomer));

    client.post()
            .uri("/").body(Mono.just(expectedCustomer), Customer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer);
  }

  @Test
  void updateCustomer_whenCustomerExists_performUpdate() {
    Customer expectedProduct = expectedCustomers.get(0);
    when(customerService.update(expectedProduct.getId(), expectedProduct)).thenReturn(Mono.just(expectedProduct));

    client.put()
            .uri("/{id}", expectedProduct.getId()).body(Mono.just(expectedProduct), Customer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Customer.class)
            .isEqualTo(expectedProduct);
  }

  @Test
  void updateCustomer_whenCustomerNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    Customer expectedCustomer = expectedCustomers.get(0);
    when(customerService.update(id, expectedCustomer)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id).body(Mono.just(expectedCustomer), Customer.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteCustomer_whenCustomerExists_performDeletion() {
    Customer customerToDelete = expectedCustomers.get(0);
    when(customerService.deleteById(customerToDelete.getId())).thenReturn(Mono.just(customerToDelete));

    client.delete()
            .uri("/{id}", customerToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteCustomer_whenIdNotExist_returnNotFound() {
    Customer customerToDelete = expectedCustomers.get(0);
    when(customerService.deleteById(customerToDelete.getId())).thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", customerToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }







}
