package org.example.simplemoneytransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Bank {
	@Id
	@GeneratedValue
	@Column(name="BANK_ID", insertable=true, updatable=false)
	@JsonIgnore
    private Long id;
	@Column(unique=true)
	private @NonNull String code;
    private @NonNull String bankName;
    private @NonNull String description;
}