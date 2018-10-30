package org.example.simplemoneytransfer.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Account;
import org.example.simplemoneytransfer.exception.AccountNotFoundException;
import org.example.simplemoneytransfer.exception.CustomException;
import org.example.simplemoneytransfer.repository.IAccountRepository;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public BigDecimal getBalanceById(Long id)
    {
    	
    	Account account =  accountRepository.findById(id)
    			                                  .orElseThrow(() -> new AccountNotFoundException(id));
    			              
    	
    	return account.getBalance(); 	
    	
    }
    
    @Transactional
    public Account updateAccountBalance(Long id, BigDecimal deltaAmount, boolean withDraw)    
    {
    	Money amount = Money.of(deltaAmount, "USD");
    	return accountRepository.findOneForUpdate(id)
    			.map(acc ->  {
    				if (amount.isNegativeOrZero())
    		       		throw new CustomException("Invalid Deposit amount for account : " + id, HttpStatus.BAD_REQUEST);
    				
    				Money updatedBalance = withDraw ? Money.of(acc.getBalance(), "USD").subtract(amount) : Money.of(acc.getBalance(), "USD").add(amount);
    				if (updatedBalance.isNegative())
    					throw new CustomException("Sorry Not sufficient Fund for account " + id, HttpStatus.BAD_REQUEST);
    				
    				acc.setBalance(updatedBalance.getNumberStripped());
    				return accountRepository.save(acc);
    			})
    			.orElseThrow(() -> new AccountNotFoundException(id));

    }
    
    
    
    
    
    
    
    
    
    
    
}