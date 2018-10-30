package org.example.simplemoneytransfer.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.money.Monetary;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

import org.example.simplemoneytransfer.dto.TransactionDTO;
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
    	try {
    	    return StreamSupport.stream(accountRepository.findByCustomersAccountId(customerId).spliterator(), true)
    			                                         .collect(Collectors.toList());
    	}
    	catch (Exception ex) {
    		throw new CustomException("Fail to get all account", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Account getAccountById(Long id)
    { 
    	try {
    		return accountRepository.findById(id)
		             .orElseThrow(() -> new AccountNotFoundException(id));
        }
    	catch (AccountNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to get one account with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    
    }

    public Account addAccount(Account newaccount)
    {  	
    	try {
    		// Create a new account with zero  balance
    		newaccount.setBalance(BigDecimal.ZERO);
    	    return accountRepository.save(newaccount);
    	}
    	catch (Exception ex) {
    		throw new CustomException("Fail to add new  account", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Account updateAccount(Account updatedAccount, Long id)
    {
    	try {
           return accountRepository.findById(id)
        		             .map(account -> {
        		            	 
        		                 account.setAccountNumber(updatedAccount.getAccountNumber());
        		                 //check about currency
        		                 this.checkAndConvertCurrencySameAccount(Optional.of(account),  Optional.of(updatedAccount), id);
        		                 account.setDescription(updatedAccount.getDescription());
        		                 account.setCustomersAccount(updatedAccount.getCustomersAccount());
        		            	 return accountRepository.save(account);
        		             })
        		           .orElseThrow(() -> new AccountNotFoundException(id));
    	}
    	catch (AccountNotFoundException ex) {
			throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to update account" + ex.getMessage(), ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    } 
    
    public void deleteAccount(Long id)
    {
        try {
        	accountRepository.deleteById(id);
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail to delete account", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
    }
    
    public BigDecimal getBalanceById(Long id)
    {
    	try {
    	    Account account =  accountRepository.findById(id)
    			                                .orElseThrow(() -> new AccountNotFoundException(id));
    			              
    	
    	    return account.getBalance(); 	
    	}
    	catch (AccountNotFoundException ex) {
			throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to get  account balance by Id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @Transactional
    public Account updateAccountBalance(Long id, BigDecimal deltaAmount, boolean withDraw)    
    {   
    	try {
    		
    	//Dummy currency for using in only number operations 
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
    	catch (CustomException ex) {
			throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to update account balance funds", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
	/**
	 * Transfer fund between two accounts.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
    @Transactional
	public List<Account> transferAccountsBalance(TransactionDTO customerTransaction)  {
    	
		Optional<Account> fromAccount = null;
		Optional<Account> toAccount = null;

		try {
            // Create Money from TransactionDTO currencyCode and balance properties for Monetary operation
			// Currency creation check for invalid currency codes.
			Money transferMoney = Money.of(customerTransaction.getAmount(), Monetary.getCurrency(customerTransaction.getCurrencyCode()));
			
			fromAccount = accountRepository.findOneForUpdate(customerTransaction.getFromAccountId());
			toAccount = accountRepository.findOneForUpdate(customerTransaction.getToAccountId());
			
			
			// check locking status
			if (!fromAccount.isPresent() || !toAccount.isPresent()) {
				throw new CustomException("Fail to lock both accounts for write", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			Money convertedTransferMoney =transferMoney;
			
			Money fromAccountMoney = Money.of(fromAccount.get().getBalance(), Monetary.getCurrency(fromAccount.get().getCurrencyCode()));
			Money toAccountMoney = Money.of(toAccount.get().getBalance(), Monetary.getCurrency(toAccount.get().getCurrencyCode()));	
			


			// check currency is the same for both accounts  if not try Currency Converting from Java Money API
			convertedTransferMoney = checkAndConvertCurrency(transferMoney, convertedTransferMoney, fromAccountMoney,
					toAccountMoney);

			 //check enough fund in source account
			Money fromAccountLeftOver = fromAccountMoney.subtract(transferMoney);
			if (fromAccountLeftOver.isNegative()) {
				throw new CustomException("Not enough Fund from source Account ", HttpStatus.BAD_REQUEST);
			}
			
			//proceed with update user current method for that job.
			// Withdraw from fromAccount
			Long fromId = fromAccount.get().getId();
            this.updateAccountBalance(fromId, transferMoney.getNumberStripped(), true);
            
            // deposit to toAccount
            Long toId = toAccount.get().getId();
            this.updateAccountBalance(toId, convertedTransferMoney.getNumberStripped(), false);
            
            // return updated accounts
            return StreamSupport.stream(accountRepository.findAllById(Arrays.asList(fromId, toId)).spliterator(), false).collect(Collectors.toList());
			
		} catch (CustomException ex) {
	          throw ex;
		} catch (Exception ex) {
	          throw new CustomException("Fail to transfer funds transaction", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
	}

	private Money checkAndConvertCurrency(Money transferMoney, Money convertedTransferMoney, Money fromAccountMoney,
			Money toAccountMoney) {
		if (!fromAccountMoney.getCurrency().equals(toAccountMoney.getCurrency())) {
			try {
			    CurrencyConversion currencyConverter = MonetaryConversions.getConversion(toAccountMoney.getCurrency());
			     convertedTransferMoney = transferMoney.with(currencyConverter);
			} 
			catch(IllegalArgumentException ex) {
		    // Backup scenario
			// This is a comment throw new CustomException("Fail to transfer Fund, the source and destination account are in different currency", 
		     //HttpStatus.BAD_REQUEST)
				throw new CustomException("Fail to transfer Fund, Currently there is no Exchange Rate Provider avaliable", HttpStatus.SERVICE_UNAVAILABLE);
			}
			catch (Exception ex) {
		          throw new CustomException("Fail to convert funds currency", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return convertedTransferMoney;
	}
	
	private void checkAndConvertCurrencySameAccount(Optional<Account> fromAccount, Optional<Account> toAccount, Long id) {
		  Account selectedAccount;
		if (fromAccount.isPresent() && toAccount.isPresent() && !fromAccount.get().getCurrencyCode().equals(toAccount.get().getCurrencyCode())) {
			try {
			    if (accountRepository.findById(id).isPresent())
				       selectedAccount = accountRepository.findById(id).get();
			    else
			    	return;
			    
				toAccount.get().setBalance(selectedAccount.getBalance());
				Money fromAccountMoney = Money.of(fromAccount.get().getBalance(), 
						Monetary.getCurrency(fromAccount.get().getCurrencyCode()));
				Money toAccountMoney = Money.of(toAccount.get().getBalance(), 
						Monetary.getCurrency(toAccount.get().getCurrencyCode()));
				
			    CurrencyConversion currencyConverter = MonetaryConversions.getConversion(toAccountMoney.getCurrency());
			    toAccountMoney = fromAccountMoney.with(currencyConverter);
			    
			    selectedAccount.setBalance(toAccountMoney.getNumberStripped());
			    selectedAccount.setCurrencyCode(toAccountMoney.getCurrency().getCurrencyCode());
			} 
			catch(IllegalArgumentException ex) {
		        // Backup scenario
				throw new CustomException("Fail to transfer Fund, Currently there is no Exchange Rate Provider avaliable", HttpStatus.SERVICE_UNAVAILABLE);
			}
			catch (Exception ex) {
		          throw new CustomException("Fail to convert funds curency", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}


   
}