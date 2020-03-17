package com.vos.bootcamp.msclients;

import com.vos.bootcamp.msclients.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MsClientsApplicationTests {

    public WebTestClient client;

    @Test
    void contextLoads() {
    }

    @Test
    public void listarTest(){


        client.get()
                .uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Customer.class)
                .hasSize(2);
    }

}
