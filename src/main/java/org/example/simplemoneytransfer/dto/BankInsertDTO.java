package org.example.simplemoneytransfer.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class BankInsertDTO {

	private @NonNull String code;
    private @NonNull String bankName;
    private @NonNull String description;
}