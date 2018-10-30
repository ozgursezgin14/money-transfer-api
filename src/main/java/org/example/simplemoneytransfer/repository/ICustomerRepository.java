package org.example.simplemoneytransfer.repository;

import org.example.simplemoneytransfer.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends CrudRepository<Customer, Long>{
	
	Iterable<Customer> findByBanksCustomerId(Long bankId);
}
