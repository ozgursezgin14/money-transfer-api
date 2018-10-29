package org.example.simplemoneytransfer.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.simplemoneytransfer.dto.CustomerDTO;
import org.example.simplemoneytransfer.dto.CustomerModifyDTO;
import org.example.simplemoneytransfer.entity.Bank;
import org.example.simplemoneytransfer.entity.Customer;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.example.simplemoneytransfer.exception.CustomerBankNotMatchException;
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
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(path="/banks/{bankId}/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(@PathVariable Long bankId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	
    	return new ResponseEntity<>(customerService.getAllCustomers(bankId).stream()
        		                         .map(b -> modelMapper.map(b, CustomerDTO.class))
        		                         .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @GetMapping(path="banks/{bankId}/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id, @PathVariable Long bankId)
    {   
    	Customer customer = customerService.getCustomerById(id);
    	if (bankService.getBankById(bankId) == null)
    		throw new BankNotFoundException(bankId);
    	else if (!customer.getBanksCustomer().getId().equals(bankId))
    		throw new CustomerBankNotMatchException(id, bankId);
    		
    	 return new ResponseEntity<>(modelMapper.map(customer, CustomerDTO.class), HttpStatus.OK);
    }

    @PostMapping(path="/banks/{bankId}/customers")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerModifyDTO newCustomer, @PathVariable Long bankId)
    {
    	Bank bank = bankService.getBankById(bankId);
       	if (bank == null)
       		throw new BankNotFoundException(bankId);
       	
    	newCustomer.setBanksCustomer(bank);
    	return new ResponseEntity<>(modelMapper.map(customerService.addCustomer(modelMapper.map(newCustomer, Customer.class)), 
        		               CustomerDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(path="/banks/{bankId}/customers/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerModifyDTO updatedCustomer, @PathVariable Long id, @PathVariable Long bankId)
    { 
    	Bank bank = bankService.getBankById(bankId);
       	if (bank == null)
       		throw new BankNotFoundException(bankId);	
       	
       	updatedCustomer.setBanksCustomer(bank);
        return new ResponseEntity<>(modelMapper.map(customerService.updateCustomer(modelMapper.map(updatedCustomer, Customer.class), id), 
	               CustomerDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(path="/banks/{bankId}/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id, @PathVariable Long bankId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (!customerService.getCustomerById(id).getBanksCustomer().getId().equals(bankId))
       		throw new CustomerBankNotMatchException(id, bankId);
       	
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}