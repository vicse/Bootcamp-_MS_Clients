package com.vos.bootcamp.msclients.routes;

import com.vos.bootcamp.msclients.handlers.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(CustomerHandler customerHandler) {

        return route(GET("/api/customers"), customerHandler::getAllCustomers)
                .andRoute(GET("/api/customers/{id}"), customerHandler::getByIdCustomer)
                .andRoute(POST("/api/customers"), customerHandler::createCustomer)
                .andRoute(PUT("//api/customers/{id}"), customerHandler::updateCustomer)
                .andRoute(DELETE("/api/customers/{id}"), customerHandler::deleteCustomer);

    }

}
