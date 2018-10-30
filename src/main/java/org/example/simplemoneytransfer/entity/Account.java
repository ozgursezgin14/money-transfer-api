package org.example.simplemoneytransfer.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ACCOUNT_ID", updatable=false, nullable=false)
    private @NonNull Long id;
	@Column(name="ACCOUNT_NUMBER", nullable=false, unique=true)
	private @NonNull String accountNumber;
	private @NonNull BigDecimal balance;
    private @NonNull String currencyCode;
    private @NonNull String description;

	@ManyToOne
	private @NonNull Customer customersAccount;
	
	public Account(Long id, String accountNumber, BigDecimal balance, String currencyCode, String description, Long customerId)
	{
		if (id == null) {
			throw new NullPointerException("id");
		} else if (accountNumber == null) {
			throw new NullPointerException("accountNumber");
		} else if (balance == null) {
			throw new NullPointerException("balance");
		} else if (currencyCode == null) {
			throw new NullPointerException("currencyCode");
		} else if (description == null) {
			throw new NullPointerException("description");
		} else if (customerId == null) {
			throw new NullPointerException("customerId");
		}
		else {
			this.id = id;
			this.accountNumber = accountNumber;
			this.balance = balance;
			this.currencyCode = currencyCode;
			this.description = description;
			this.customersAccount = new Customer(customerId, "", "", "", 0L);
		}
	}
}