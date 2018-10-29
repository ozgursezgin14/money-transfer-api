package org.example.simplemoneytransfer.dto;

import org.example.simplemoneytransfer.entity.Bank;

import lombok.Data;
import lombok.NonNull;

@Data
public class CustomerModifyDTO {

	private @NonNull String email;
    private @NonNull String customerName;
    private @NonNull String description;
    private @NonNull Bank banksCustomer;
}