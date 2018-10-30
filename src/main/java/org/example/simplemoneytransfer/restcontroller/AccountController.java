package org.example.simplemoneytransfer.restcontroller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.example.simplemoneytransfer.dto.AccountDTO;
import org.example.simplemoneytransfer.dto.AccountModifyDTO;
import org.example.simplemoneytransfer.entity.Account;
import org.example.simplemoneytransfer.entity.Customer;
import org.example.simplemoneytransfer.exception.AccountCustomerNotMatchException;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.example.simplemoneytransfer.exception.CustomerBankNotMatchException;
import org.example.simplemoneytransfer.exception.CustomerNotFoundException;
import org.example.simplemoneytransfer.service.AccountService;
import org.example.simplemoneytransfer.service.BankService;
import org.example.simplemoneytransfer.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(path="/banks/{bankId}/customers/{customerId}/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@PathVariable Long bankId, @PathVariable Long customerId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId)== null)
       		throw new CustomerNotFoundException(customerId);
       	
    	return new ResponseEntity<>(accountService.getAllAccounts(customerId).stream()
        		                         .map(b -> modelMapper.map(b, AccountDTO.class))
        		                         .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @GetMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    {   
    	Account account = accountService.getAccountById(id);
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
    	else if (!account.getCustomersAccount().getId().equals(customerId))
    		throw new AccountCustomerNotMatchException(id, customerId);
    		
    	 return new ResponseEntity<>(modelMapper.map(account, AccountDTO.class), HttpStatus.OK);
    }

    @GetMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}/balance")
    public ResponseEntity<BigDecimal> getDepositById(@PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    {   
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
    	else if (!accountService.getAccountById(id).getCustomersAccount().getId().equals(customerId))
    		throw new AccountCustomerNotMatchException(id, customerId);
    		
    	 return new ResponseEntity<>(accountService.getBalanceById(id), HttpStatus.OK);
    }
    
    @PostMapping(path="/banks/{bankId}/customers/{customerId}/accounts")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountModifyDTO newAccount, @PathVariable Long bankId, @PathVariable Long customerId)
    {
    	Customer customer = customerService.getCustomerById(customerId);
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);

       	
    	newAccount.setCustomersAccount(customer);    	
    	return new ResponseEntity<>(modelMapper.map(accountService.addAccount(modelMapper.map(newAccount, Account.class)), 
	               AccountDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountModifyDTO updatedAccount, @PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    { 
    	Customer customer = customerService.getCustomerById(customerId);
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
       	
       	updatedAccount.setCustomersAccount(customer);
        return new ResponseEntity<>(modelMapper.map(accountService.updateAccount(modelMapper.map(updatedAccount, Account.class), id), 
	               AccountDTO.class), HttpStatus.OK);
    }

    @PutMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}/deposit/{amount}")
    public ResponseEntity<AccountDTO> deposit(@PathVariable BigDecimal amount, @PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    { 
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
    	else if (!accountService.getAccountById(id).getCustomersAccount().getId().equals(customerId))
    		throw new AccountCustomerNotMatchException(id, customerId);
    
       	
        return new ResponseEntity<>(modelMapper.map(accountService.updateAccountBalance(id, amount, false), 
	               AccountDTO.class), HttpStatus.OK);
    }
    
    @PutMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}/withdraw/{amount}")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable BigDecimal amount, @PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    { 
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
    	else if (!accountService.getAccountById(id).getCustomersAccount().getId().equals(customerId))
    		throw new AccountCustomerNotMatchException(id, customerId);

       	
        return new ResponseEntity<>(modelMapper.map(accountService.updateAccountBalance(id, amount, true), 
	               AccountDTO.class), HttpStatus.OK);
    }
    
    @DeleteMapping(path="/banks/{bankId}/customers/{customerId}/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id, @PathVariable Long bankId, @PathVariable Long customerId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(customerId) == null)
       		throw new CustomerNotFoundException(customerId);
       	else if (!customerService.getCustomerById(customerId).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(customerId, bankId);
    	else if (!accountService.getAccountById(id).getCustomersAccount().getId().equals(customerId))
    		throw new AccountCustomerNotMatchException(id, customerId);
       	
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}