package org.example.simplemoneytransfer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Data
public class BankModifyDTO {

	private @NotEmpty @NonNull String code;
    private @NotEmpty @NonNull String bankName;
    private @NotNull @NonNull String description;
}