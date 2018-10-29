package org.example.simplemoneytransfer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Account;
import org.example.simplemoneytransfer.exception.AccountNotFoundException;
import org.example.simplemoneytransfer.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
   
	@Autowired
	private IAccountRepository accountRepository;
	
    public List<Account> getAllAccounts(Long customerId)
    {
    	return StreamSupport.stream(accountRepository.findByCustomersAccountId(customerId).spliterator(), true)
    			            .collect(Collectors.toList());
    }

    public Account getAccountById(Long id)
    {
    	return accountRepository.findById(id)
    			             .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account addAccount(Account newaccount)
    {  	
    	return accountRepository.save(newaccount);
    }

    public Account updateAccount(Account updatedAccount, Long id)
    {
        return accountRepository.findById(id)
        		             .map(account -> {
        		                 account.setAccountNumber(updatedAccount.getAccountNumber());
        		                 account.setBalance(updatedAccount.getBalance());
        		                 account.setCurrencyCode(updatedAccount.getCurrencyCode());
        		                 account.setDescription(updatedAccount.getDescription());
        		                 account.setCustomersAccount(updatedAccount.getCustomersAccount());
        		            	 return accountRepository.save(account);
        		             })
        		           .orElseThrow(() -> new AccountNotFoundException(id));
        		             
    } 
    
    public void deleteAccount(Long id)
    {
        accountRepository.deleteById(id);
    }
}