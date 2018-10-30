package org.example.simplemoneytransfer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Customer;
import org.example.simplemoneytransfer.exception.CustomException;
import org.example.simplemoneytransfer.exception.CustomerNotFoundException;
import org.example.simplemoneytransfer.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
   
	@Autowired
	private ICustomerRepository customerRepository;
	
    public List<Customer> getAllCustomers(Long bankId)
    {
    	
     	try {
     		return StreamSupport.stream(customerRepository.findByBanksCustomerId(bankId).spliterator(), true)
		            .collect(Collectors.toList());
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail to get all customers", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Customer getCustomerById(Long id)
    {
    	
     	try {
     		return customerRepository.findById(id)
		             .orElseThrow(() -> new CustomerNotFoundException(id));
        }
    	catch (CustomerNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to get one customer with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Customer addCustomer(Customer newcustomer)
    {  	
    	
     	try {
     		return customerRepository.save(newcustomer);
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail to add new customer", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Customer updateCustomer(Customer updatedCustomer, Long id)
    {
       
     	try {
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
    	catch (CustomerNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to update customer", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        		             
    } 
    
    public void deleteCustomer(Long id)
    {
               
     	try {
     		customerRepository.deleteById(id);
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail to delete customer with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}