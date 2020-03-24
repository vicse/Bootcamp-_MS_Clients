package com.vos.bootcamp.msclients.services;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.models.TypeCustomer;
import com.vos.bootcamp.msclients.repositories.ICustomerRepository;
import com.vos.bootcamp.msclients.repositories.ITypeCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CustomerTypeServiceTest {

  private final TypeCustomer typeCustomer1 = TypeCustomer.builder().name("Type of customer 1").build();
  private final TypeCustomer typeCustomer2 = TypeCustomer.builder().name("Type of customer 2").build();
  private final TypeCustomer typeCustomer3 = TypeCustomer.builder().name("Type of customer 3").build();

  @Mock
  private ITypeCustomerRepository typeCustomerRepository;

  private ITypeCustomerService typeCustomerService;

  @BeforeEach
  void SetUp(){
    typeCustomerService = new TypeCustomerServiceImpl(typeCustomerRepository) {
    };
  }

  @Test
  void getAll() {
    when(typeCustomerRepository.findAll()).thenReturn(Flux.just(typeCustomer1, typeCustomer2, typeCustomer3));

    Flux<TypeCustomer> actual = typeCustomerService.findAll();

    assertResults(actual, typeCustomer1, typeCustomer2, typeCustomer3);
  }

  @Test
  void getById_whenIdExists_returnCorrectTypeCustomer() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.just(typeCustomer1));

    Mono<TypeCustomer> actual = typeCustomerService.findById(typeCustomer1.getId());

    assertResults(actual, typeCustomer1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.empty());

    Mono<TypeCustomer> actual = typeCustomerService.findById(typeCustomer1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(typeCustomerRepository.save(typeCustomer1)).thenReturn(Mono.just(typeCustomer1));

    Mono<TypeCustomer> actual = typeCustomerService.save(typeCustomer1);

    assertResults(actual, typeCustomer1);
  }

  @Test
  void update_whenIdExists_returnUpdatedTypeCustomer() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.just(typeCustomer1));
    when(typeCustomerRepository.save(typeCustomer1)).thenReturn(Mono.just(typeCustomer1));

    Mono<TypeCustomer> actual = typeCustomerService.update(typeCustomer1.getId(), typeCustomer1);

    assertResults(actual, typeCustomer1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.empty());

    Mono<TypeCustomer> actual = typeCustomerService.update(typeCustomer1.getId(), typeCustomer1);

    assertResults(actual);
  }

  @Test
  void delete_whenTypeCustomerExists_performDeletion() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.just(typeCustomer1));
    when(typeCustomerRepository.delete(typeCustomer1)).thenReturn(Mono.empty());

    Mono<TypeCustomer> actual = typeCustomerService.deleteById(typeCustomer1.getId());

    assertResults(actual, typeCustomer1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(typeCustomerRepository.findById(typeCustomer1.getId())).thenReturn(Mono.empty());

    Mono<TypeCustomer> actual = typeCustomerService.deleteById(typeCustomer1.getId());

    assertResults(actual);
  }



  private void assertResults(Publisher<TypeCustomer> publisher, TypeCustomer... expectedTypesCustomer) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedTypesCustomer)
            .verifyComplete();
  }


}
