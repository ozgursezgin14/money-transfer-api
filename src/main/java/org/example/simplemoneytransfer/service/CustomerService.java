package org.example.simplemoneytransfer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Customer;
import org.example.simplemoneytransfer.exception.CustomerNotFoundException;
import org.example.simplemoneytransfer.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
   
	@Autowired
	private ICustomerRepository customerRepository;
	
    public List<Customer> getAllCustomers(Long bankId)
    {
    	return StreamSupport.stream(customerRepository.findByBanksCustomerId(bankId).spliterator(), true)
    			            .collect(Collectors.toList());
    }

    public Customer getCustomerById(Long id)
    {
    	return customerRepository.findById(id)
    			             .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer addCustomer(Customer newcustomer)
    {  	
    	return customerRepository.save(newcustomer);
    }

    public Customer updateCustomer(Customer updatedCustomer, Long id)
    {
        return customerRepository.findById(id)
        		             .map(customer -> {
        		            	 customer.setCustomerName(updatedCustomer.getCustomerName());
        		            	 customer.setEmail(updatedCustomer.getEmail());
        		            	 customer.setDescription(updatedCustomer.getDescription());
        		            	 customer.setBanksCustomer(updatedCustomer.getBanksCustomer());
        		            	 return customerRepository.save(customer);
        		             })
        		           .orElseThrow(() -> new CustomerNotFoundException(id));
        		             
    } 
    
    public void deleteCustomer(Long id)
    {
        customerRepository.deleteById(id);
    }
}