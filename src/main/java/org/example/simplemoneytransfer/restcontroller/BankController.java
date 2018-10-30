package org.example.simplemoneytransfer.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.example.simplemoneytransfer.dto.BankDTO;
import org.example.simplemoneytransfer.dto.BankModifyDTO;
import org.example.simplemoneytransfer.entity.Bank;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.example.simplemoneytransfer.service.BankService;
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
public class BankController {
    
    @Autowired
    private BankService bankService;
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(path="/banks")
    public ResponseEntity<List<BankDTO>> getAllBanks()
    {
        return  new ResponseEntity<>(bankService.getAllBanks().stream()
        		                         .map(b -> modelMapper.map(b, BankDTO.class))
        		                         .collect(Collectors.toList()), HttpStatus.OK);
    }
    
    @GetMapping(path="banks/{id}")
    public ResponseEntity<BankDTO> getBankById(@PathVariable Long id)
    {
    	if (bankService.getBankById(id) == null)
       		throw new BankNotFoundException(id);
    	
    	 return new ResponseEntity<>(modelMapper.map(bankService.getBankById(id), BankDTO.class), HttpStatus.OK);
    }

    @PostMapping(path="/banks")
    public ResponseEntity<BankDTO> addBank(@Valid @RequestBody BankModifyDTO newBank)
    {
        return new ResponseEntity<>(modelMapper.map(bankService.addBank(modelMapper.map(newBank, Bank.class)), 
        		               BankDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(path="/banks/{id}")
    public ResponseEntity<BankDTO> updateBank(@Valid @RequestBody BankModifyDTO updatedBank, @PathVariable Long id)
    {
    	if (bankService.getBankById(id) == null)
       		throw new BankNotFoundException(id);
    	
        return new ResponseEntity<>(modelMapper.map(bankService.updateBank(modelMapper.map(updatedBank, Bank.class), id), 
	               BankDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(path="/banks/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id)
    {
    	if (bankService.getBankById(id) == null)
       		throw new BankNotFoundException(id);
    	
    	bankService.deleteBank(id);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}