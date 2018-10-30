package org.example.simplemoneytransfer.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.example.simplemoneytransfer.entity.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends CrudRepository<Account, Long>{
	
	Iterable<Account> findByCustomersAccountId(Long bankId);
	Optional<Account> findByAccountNumber(String number);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.id = :id")
	Optional<Account> findOneForUpdate(@Param("id") Long id);
}
