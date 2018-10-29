package org.example.simplemoneytransfer.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class BankModifyDTO {

	private @NonNull String code;
    private @NonNull String bankName;
    private @NonNull String description;
}