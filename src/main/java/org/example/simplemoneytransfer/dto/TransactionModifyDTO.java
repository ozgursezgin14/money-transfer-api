package org.example.simplemoneytransfer.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Data
public class TransactionModifyDTO {
	
	private @Digits(fraction = 2, integer = 10) @NotNull @NonNull BigDecimal amount;
    private @NotEmpty @NonNull Long toAccountId;

}