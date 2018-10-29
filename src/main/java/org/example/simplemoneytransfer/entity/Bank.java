package org.example.simplemoneytransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Bank {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BANK_ID")
    private Long id;
	@Column(unique=true)
	private @NonNull String code;
    private @NonNull String bankName;
    private @NonNull String description;
    
	public Bank(Long id, String code, String bankName, String description)
	{
		if (id == null) {
			throw new NullPointerException("id");
		} else if (code == null) {
			throw new NullPointerException("code");
		} else if (bankName == null) {
			throw new NullPointerException("bankName");
		} else if (description == null) {
			throw new NullPointerException("description");
		} else {
			this.id = id;
			this.code = code;
			this.bankName = bankName;
			this.description = description;
		}
	}
}