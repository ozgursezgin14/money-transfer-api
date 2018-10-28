package org.example.simplemoneytransfer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.simplemoneytransfer.entity.Bank;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.example.simplemoneytransfer.repository.IBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
   
	@Autowired
	private IBankRepository bankRepository;
	
    public List<Bank> getAllBanks()
    {
    	return StreamSupport.stream(bankRepository.findAll().spliterator(), true)
    			            .collect(Collectors.toList());
    }

    public Bank getBankById(Long id)
    {
    	return bankRepository.findById(id)
    			             .orElseThrow(() -> new BankNotFoundException(id));
    }

    public Bank addBank(Bank newbank)
    {  	
    	return bankRepository.save(newbank);
    }

    public Bank updateBank(Bank updatedBank, Long id)
    {
        return bankRepository.findById(id)
        		             .map(bank -> {
        		            	 bank.setBankName(updatedBank.getBankName());
        		            	 bank.setCode(updatedBank.getCode());
        		            	 bank.setDescription(updatedBank.getDescription());
        		            	 return bankRepository.save(bank);
        		             })
        		           .orElseThrow(() -> new BankNotFoundException(id));
        		             
    } 
    
    public void deleteBank(Long id)
    {
        bankRepository.deleteById(id);
    }
}