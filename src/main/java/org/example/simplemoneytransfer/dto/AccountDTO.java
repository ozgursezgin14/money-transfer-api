package org.example.simplemoneytransfer.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import org.example.simplemoneytransfer.entity.Customer;

import lombok.Data;
import lombok.NonNull;

@Data
public class AccountDTO {

    private @NonNull Long id;
	private @NonNull String accountNumber;
	private @Digits(fraction = 2, integer = 10) @NonNull BigDecimal balance;
    private @NonNull String currencyCode;
    private @NonNull String description;
    
    private @NonNull Customer customersAccount;
}