package org.example.simplemoneytransfer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.example.simplemoneytransfer.entity.Customer;

import lombok.Data;
import lombok.NonNull;

@Data
public class AccountModifyDTO {

	private @NotEmpty @NonNull String accountNumber;
    private @NotEmpty @NonNull String currencyCode;
    private @NotNull  @NonNull String description;
    
    private @NonNull Customer customersAccount;
}