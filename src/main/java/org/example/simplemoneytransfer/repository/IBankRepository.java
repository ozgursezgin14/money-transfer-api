package org.example.simplemoneytransfer.repository;

import org.example.simplemoneytransfer.entity.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankRepository extends CrudRepository<Bank, Long>{ }
