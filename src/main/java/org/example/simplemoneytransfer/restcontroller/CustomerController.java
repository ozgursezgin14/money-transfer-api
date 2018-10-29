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
    public List<CustomerDTO> getAllCustomers(@PathVariable Long bankId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	
    	return customerService.getAllCustomers(bankId).stream()
        		                         .map(b -> modelMapper.map(b, CustomerDTO.class))
        		                         .collect(Collectors.toList());
    }
    
    @GetMapping(path="banks/{bankId}/customers/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id, @PathVariable Long bankId)
    {   
    	Customer customer = customerService.getCustomerById(id);
    	if (bankService.getBankById(bankId) == null)
    		throw new BankNotFoundException(bankId);
    	else if (customer.getBanksCustomer().getId() != bankId)
    		throw new CustomerBankNotMatchException(id, bankId);
    		
    	 return modelMapper.map(customer, CustomerDTO.class);
    }

    @PostMapping(path="/banks/{bankId}/customers")
    public CustomerDTO addCustomer(@RequestBody CustomerModifyDTO newCustomer, @PathVariable Long bankId)
    {
    	Bank bank = bankService.getBankById(bankId);
       	if (bank == null)
       		throw new BankNotFoundException(bankId);
       	
    	newCustomer.setBanksCustomer(bank);
    	return modelMapper.map(customerService.addCustomer(modelMapper.map(newCustomer, Customer.class)), 
        		               CustomerDTO.class);
    }

    @PutMapping(path="/banks/{bankId}/customers/{id}")
    public CustomerDTO updateCustomer(@RequestBody CustomerModifyDTO updatedCustomer, @PathVariable Long id, @PathVariable Long bankId)
    { 
    	Bank bank = bankService.getBankById(bankId);
       	if (bank == null)
       		throw new BankNotFoundException(bankId);	
       	
       	updatedCustomer.setBanksCustomer(bank);
        return modelMapper.map(customerService.updateCustomer(modelMapper.map(updatedCustomer, Customer.class), id), 
	               CustomerDTO.class);
    }

    @DeleteMapping(path="/banks/{bankId}/customers/{id}")
    public void deleteCustomer(@PathVariable Long id, @PathVariable Long bankId)
    {
       	if (bankService.getBankById(bankId) == null)
       		throw new BankNotFoundException(bankId);
       	else if (customerService.getCustomerById(id).getBanksCustomer().getId() != bankId)
       		throw new CustomerBankNotMatchException(id, bankId);
       	
        customerService.deleteCustomer(id);
    }
}