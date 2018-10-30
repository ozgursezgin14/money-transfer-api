package org.example.simplemoneytransfer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.example.simplemoneytransfer.entity.Bank;

import lombok.Data;
import lombok.NonNull;

@Data
public class CustomerModifyDTO {

	private @NotEmpty @NonNull String email;
    private @NotEmpty @NonNull String customerName;
    private @NotNull @NonNull String description;
    
    private @NonNull Bank banksCustomer;
}