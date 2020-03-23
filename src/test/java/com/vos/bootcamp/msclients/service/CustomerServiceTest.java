package com.vos.bootcamp.msclients.service;

import static org.mockito.Mockito.when;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.repositories.ICustomerRepository;
import com.vos.bootcamp.msclients.services.CustomerServiceImpl;
import com.vos.bootcamp.msclients.services.ICustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

  private final TypeCustomer typeCustomer1 = TypeCustomer.builder().name("Type of customer 1").build();

  private final TypeCustomer typeCustomer2 = TypeCustomer.builder().name("Type of customer 2").build();

  private final Customer customer1 = Customer.builder().names("Vicse").surnames("Ore Soto").numIdentityDoc("75772936")
          .email("vicseore@gmail.com").phoneNumber("945026794").address("Calle 1 El Agustino").typeCustomer(typeCustomer1).build();

  private final Customer customer2 = Customer.builder().names("Cristian").surnames("Huaynates Soto").numIdentityDoc("34256278")
          .email("cheles@gmail.com").phoneNumber("990123568").address("Los frailes 401").typeCustomer(typeCustomer2).build();

  private final Customer customer3 = Customer.builder().names("Carlos").surnames("Huaynates Soto").numIdentityDoc("70347856")
          .email("galdoz@gmail.com").phoneNumber("993412354").address("Los frailes 401").typeCustomer(typeCustomer2).build();


  @Mock
  private ICustomerRepository customerRepository;

  private ICustomerService customerService;

  @BeforeEach
  void SetUp(){
    customerService = new CustomerServiceImpl(customerRepository) {
    };
  }

  @Test
  void getAll() {
    when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2, customer3));

    Flux<Customer> actual = customerService.findAll();

    assertResults(actual, customer1, customer2, customer3);
  }

  @Test
  void getById_whenIdExists_returnCorrectCustomer() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.just(customer1));

    Mono<Customer> actual = customerService.findById(customer1.getId());

    assertResults(actual, customer1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.empty());

    Mono<Customer> actual = customerService.findById(customer1.getId());

    assertResults(actual);
  }

  @Test
  void searchByNumDocIdentity() {
    final String numDoc = "75772936";
    when(customerRepository.findByNumIdentityDoc(numDoc)).thenReturn(Mono.just(customer1));

    Mono<Customer> actual = customerService.findByNumDoc(numDoc);

    assertResults(actual, customer1);
  }

  @Test
  void getCustomerTypeByNumDocIdentity() {
    final String numDoc = "75772936";
    when(customerRepository.findByNumIdentityDoc(numDoc)).thenReturn(Mono.just(customer1));

    Mono<TypeCustomer> actual = customerService.getTypeCustomer(numDoc);

    assertResults(actual, customer1.getTypeCustomer());
  }

  @Test
  void validateIfCustomerExists() {
    final String numDoc = "75772936";
    when(customerRepository.existsByNumIdentityDoc(numDoc)).thenReturn(Mono.just(true));

    Mono<Boolean> actual = customerService.existsCustomer(numDoc);

    assertResults(actual, true);
  }

  @Test
  void create() {
    when(customerRepository.save(customer1)).thenReturn(Mono.just(customer1));

    Mono<Customer> actual = customerService.save(customer1);

    assertResults(actual, customer1);
  }

  @Test
  void update_whenIdExists_returnUpdatedCustomer() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.just(customer1));
    when(customerRepository.save(customer1)).thenReturn(Mono.just(customer1));

    Mono<Customer> actual = customerService.update(customer1.getId(), customer1);

    assertResults(actual, customer1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.empty());

    Mono<Customer> actual = customerService.update(customer1.getId(), customer1);

    assertResults(actual);
  }

  @Test
  void delete_whenCustomerExists_performDeletion() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.just(customer1));
    when(customerRepository.delete(customer1)).thenReturn(Mono.empty());

    Mono<Customer> actual = customerService.deleteById(customer1.getId());

    assertResults(actual, customer1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(customerRepository.findById(customer1.getId())).thenReturn(Mono.empty());

    Mono<Customer> actual = customerService.deleteById(customer1.getId());

    assertResults(actual);
  }

  private void assertResults(Publisher<Customer> publisher, Customer... expectedCustomers) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCustomers)
            .verifyComplete();
  }

  private void assertResults(Mono<Boolean> actual, boolean b) {
    StepVerifier
            .create(actual)
            .expectNext(b)
            .verifyComplete();
  }

  private void assertResults(Mono<TypeCustomer> actual, TypeCustomer typeCustomer) {
    StepVerifier
            .create(actual)
            .expectNext(typeCustomer)
            .verifyComplete();
  }



}
