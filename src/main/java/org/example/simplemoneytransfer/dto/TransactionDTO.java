package org.example.simplemoneytransfer.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.Data;
import lombok.NonNull;

@Data
public class TransactionDTO {

    private @NonNull String currencyCode;
	private @Digits(fraction = 2, integer = 10) @NonNull BigDecimal amount;
    private @NonNull Long fromAccountId;
    private @NonNull Long toAccountId;

}