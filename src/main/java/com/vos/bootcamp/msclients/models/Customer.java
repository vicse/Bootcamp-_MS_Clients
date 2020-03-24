package com.vos.bootcamp.msclients.models;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ms_customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

  @EqualsAndHashCode.Exclude
  @Id
  private String id;

  @NotBlank(message = "'Names' are required")
  private String names;

  @NotBlank(message = "'Surnames' are required")
  private String surnames;

  @NotBlank(message = "'numIdentityDoc' are required")
  private String numIdentityDoc;

  @NotBlank(message = "'email' are required")
  private String email;

  @NotBlank(message = "'Phone Number' is required")
  private String phoneNumber;

  @NotBlank(message = "'Address' is required")
  private String address;

  @Valid
  @DBRef
  private TypeCustomer typeCustomer;

}
