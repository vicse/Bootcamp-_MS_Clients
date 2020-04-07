package com.vos.bootcamp.msclients.integration.controller;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.repositories.ICustomerRepository;
import com.vos.bootcamp.msclients.repositories.ITypeCustomerRepository;
import com.vos.bootcamp.msclients.services.ICustomerService;
import com.vos.bootcamp.msclients.services.ITypeCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerIntegrationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ICustomerRepository customerRepository;

  @Autowired
  private ITypeCustomerRepository customerTypeRepository;

  private final TypeCustomer typeCustomer1 = TypeCustomer.builder().name("PERSONAL").build();
  private final TypeCustomer typeCustomer2 = TypeCustomer.builder().name("EMPRESARIAL").build();
  private final TypeCustomer typeCustomer3 = TypeCustomer.builder().name("PERSONA VIP").build();
  private final TypeCustomer typeCustomer4 = TypeCustomer.builder().name("PYME").build();
  private final TypeCustomer typeCustomer5 = TypeCustomer.builder().name("CORPORATIVO").build();

  private WebTestClient client;

  @Autowired
  private ReactiveMongoTemplate mongoTemplate;

  private List<Customer> expectedCustomers;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .baseUrl("/api/customers")
            .build();

    mongoTemplate.dropCollection("ms_customers").subscribe();
    mongoTemplate.dropCollection("ms_customers_typeCustomer").subscribe();

    Flux<Customer> initData = Flux.just(typeCustomer1,typeCustomer2,typeCustomer3,typeCustomer4,typeCustomer5)
            .flatMap(customerTypeRepository::save)
            .thenMany(
              Flux.just(
                Customer.builder().id("1").names("Vicse").surnames("Ore Soto").numIdentityDoc("75772936")
                        .email("vicseore@gmail.com").phoneNumber("945026794").address("Calle 1 - El Agustino")
                        .typeCustomer(typeCustomer1).build(),
                Customer.builder().id("2").names("Cristian").surnames("Huaynates Soto").numIdentityDoc("34256278")
                        .email("cheles@gmail.com").phoneNumber("990123568").address("Calle Los frailes 401")
                        .typeCustomer(typeCustomer2).build(),
                Customer.builder().id("3").names("Carlos").surnames("Huaynates Soto").numIdentityDoc("70347856")
                        .email("galdoz@gmail.com").phoneNumber("993412354").address("Calle Los frailes 401")
                        .typeCustomer(typeCustomer3).build(),
                Customer.builder().id("4").names("Gianella").surnames("De La Cruz Soto").numIdentityDoc("78096543")
                        .email("giane@gmail.com").phoneNumber("994678900").address("Pjse. Los Geranios 126")
                        .typeCustomer(typeCustomer4).build(),
                Customer.builder().id("5").names("Maria Isabel").surnames("Estrada Aguirre").numIdentityDoc("67890123")
                        .email("marisa96@gmail.com").phoneNumber("968456348").address("Av. La mar 446 - San Miguel")
                        .typeCustomer(typeCustomer5).build())
              .flatMap(customerRepository::save))
            .thenMany(customerRepository.findAll());

    expectedCustomers = initData.collectList().block();

  }

  @Test
  void getAllCustomers() {
    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Customer.class)
            .isEqualTo(expectedCustomers)
    ;
  }

  @Test
  void getCustomerById_whenCustomerExists_returnCorrectCustomer() {
    Customer expectedCustomer = expectedCustomers.get(0);

    client.get()
            .uri("/{id}", expectedCustomer.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer)
    ;
  }

  @Test
  void getCustomerById_whenCustomerNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }

  @Test
  void searchByNumDocIdentity_whenCustomerExists_returnCorrectCustomer() {
    String numDoc = "75772936";
    Customer expectedCustomer = expectedCustomers.get(0);

    client.get()
            .uri("/param/identityDoc/{numDoc}", numDoc)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer)
    ;
  }

  @Test
  void searchByNumDocIdentity_whenCustomerNotExist_returnNotFound() {
    String numDoc = "NOT_EXIST_ID";

    client.get()
            .uri("/param/identityDoc/{numDoc}", numDoc)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }

  @Test
  void addCustomer() {
    Customer expectedCustomer = expectedCustomers.get(0);

    client.post()
            .uri("/")
            .body(Mono.just(expectedCustomer), Customer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer)
    ;
  }

  @Test
  void addCustomer_whenCustomerIsInvalid_returnBadRequest() {
    Customer customer = Customer.builder().names("names test").surnames("surnames test").numIdentityDoc("000").build();

    client.post()
            .uri("/")
            .body(Mono.just(customer), Customer.class)
            .exchange()
            .expectStatus()
            .isBadRequest()
    ;
  }


  @Test
  void updateCustomer_whenCustomerExists_performUpdate() {
    Customer expectedCustomer = expectedCustomers.get(0);

    client.put()
            .uri("/{id}", expectedCustomer.getId())
            .body(Mono.just(expectedCustomer), Customer.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Customer.class)
            .isEqualTo(expectedCustomer)
    ;
  }

  @Test
  void updateCustomer_whenCustomerNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    Customer expectedCustomer = expectedCustomers.get(0);

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedCustomer), Customer.class)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }


  @Test
  void deleteCustomer_whenCustomerExists_performDeletion() {
    Customer customerToDelete = expectedCustomers.get(0);

    client.delete()
            .uri("/{id}", customerToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk()
    ;
  }

  @Test
  void deleteCustomer_whenIdNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";

    client.delete()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }


}
