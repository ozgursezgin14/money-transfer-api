package org.example.simplemoneytransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Customer {
	@Id
	@GeneratedValue
	@Column(name="CUSTOMER_ID")
    private Long id;
	@Column(unique=true)
	private @NonNull String email;
    private @NonNull String customerName;
    private @NonNull String description;
    
	@ManyToOne
	private @NonNull Bank banksCustomer;
	
	public Customer(Long id, String email, String customerName, String description, Long bankId)
	{
		if (id == null) {
			throw new NullPointerException("id");
		} else if (email == null) {
			throw new NullPointerException("email");
		} else if (customerName == null) {
			throw new NullPointerException("customerName");
		} else if (description == null) {
			throw new NullPointerException("description");
		} else if (bankId == null) {
			throw new NullPointerException("bankId");
		}
		else {
			this.id = id;
			this.email = email;
			this.customerName = customerName;
			this.description = description;
			this.banksCustomer = new Bank(bankId, "", "", "");
		}
	}
}