package com.vos.bootcamp.msclients;

import com.vos.bootcamp.msclients.models.Customer;
import com.vos.bootcamp.msclients.services.ICustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.List;

@SpringBootTest()
class MsClientsApplicationTests {

    public WebTestClient client;

    @Autowired
    private ICustomerService service;

    @Test
    void contextLoads() {
    }

    /*@Test
    public void listarTest(){

        client.get()
                .uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Customer.class)
                .consumeWith(response -> {
                    List<Customer> productos = response.getResponseBody();
                    productos.forEach(p ->{
                        System.out.println(p.getNames());
                    });
                    Assertions.assertThat(productos.size() == 9).isTrue();
                });


                //.hasSize(2);
    }

     */

    @Test
    void findByIdClient() {
        Customer customer = service.findById("5e6ff23ea903f63e52bc8f6c").block();
        client.get().uri("/customers/{id}", Collections.singletonMap("id", customer.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

}
