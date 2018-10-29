package org.example.simplemoneytransfer.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ErrorDataDTO {

	private final int errorCode;
	private @NonNull String message;
}