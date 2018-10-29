package org.example.simplemoneytransfer.repository;

import org.example.simplemoneytransfer.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends CrudRepository<Account, Long>{
	
	public Iterable<Account> findByCustomersAccountId(Long bankId);
}
