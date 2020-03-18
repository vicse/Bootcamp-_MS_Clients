package com.vos.bootcamp.msclients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "ms_customers_typeCustomer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TypeCustomer {

    @Id
    private String id;

    @NotBlank(message = "'Names' are required")
    private String name;


}
