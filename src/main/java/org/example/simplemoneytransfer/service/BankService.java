package org.example.simplemoneytransfer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Bank;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.example.simplemoneytransfer.exception.CustomException;
import org.example.simplemoneytransfer.repository.IBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BankService {
   
	@Autowired
	private IBankRepository bankRepository;
	
    public List<Bank> getAllBanks()
    {
    	
     	try {
     		return StreamSupport.stream(bankRepository.findAll().spliterator(), true)
		            .collect(Collectors.toList());
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail to get all banks", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Bank getBankById(Long id)
    {
    	
     	try {
     		return bankRepository.findById(id)
		             .orElseThrow(() -> new BankNotFoundException(id));
        }
    	catch (BankNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to get one bank with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Bank addBank(Bank newbank)
    {  	
    	
     	try {
     		return bankRepository.save(newbank);
        }
    	catch (Exception ex) {
    		throw new CustomException("Fail add new  bank", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    public Bank updateBank(Bank updatedBank, Long id)
    {

     	try {
            return bankRepository.findById(id)
		             .map(bank -> {
		            	 bank.setBankName(updatedBank.getBankName());
		            	 bank.setCode(updatedBank.getCode());
		            	 bank.setDescription(updatedBank.getDescription());
		            	 return bankRepository.save(bank);
		             })
		           .orElseThrow(() -> new BankNotFoundException(id));
        }
    	catch (BankNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to update bank with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        		             
    } 
    
    public void deleteBank(Long id)
    {
       
     	try {
     		 bankRepository.deleteById(id);
        }
    	catch (BankNotFoundException ex) {
	         throw ex;
		}
    	catch (Exception ex) {
    		throw new CustomException("Fail to delete bank with id", ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}