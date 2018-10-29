package org.example.simplemoneytransfer.dto;

import java.math.BigDecimal;

import org.example.simplemoneytransfer.entity.Customer;

import lombok.Data;
import lombok.NonNull;

@Data
public class AccountDTO {

    private @NonNull Long id;
	private @NonNull String accountNumber;
	private @NonNull BigDecimal balance;
    private @NonNull String currencyCode;
    private @NonNull String description;
    
    private @NonNull Customer customersAccount;
}