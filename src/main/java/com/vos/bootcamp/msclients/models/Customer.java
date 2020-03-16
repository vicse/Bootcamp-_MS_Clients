package com.vos.bootcamp.msclients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "ms_customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    @Id
    private String id;

    @NotBlank(message = "'Names' are required")
    private String names;

    @NotBlank(message = "'Surnames' are required")
    private String surnames;

    @NotBlank(message = "'Phone Number' is required")
    private String phoneNumber;

    @NotBlank(message = "'Address' is required")
    private String address;
}
