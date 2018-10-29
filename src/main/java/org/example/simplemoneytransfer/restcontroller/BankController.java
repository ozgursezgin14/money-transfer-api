package org.example.simplemoneytransfer.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.example.simplemoneytransfer.dto.BankDTO;
import org.example.simplemoneytransfer.dto.BankModifyDTO;
import org.example.simplemoneytransfer.entity.Bank;
import org.example.simplemoneytransfer.service.BankService;
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
public class BankController {
    
    @Autowired
    private BankService bankService;
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(path="/banks")
    public List<BankDTO> getAllBanks()
    {
        return bankService.getAllBanks().stream()
        		                         .map(b -> modelMapper.map(b, BankDTO.class))
        		                         .collect(Collectors.toList());
    }
    
    @GetMapping(path="banks/{id}")
    public BankDTO getBankById(@PathVariable Long id)
    {
    	 return modelMapper.map(bankService.getBankById(id), BankDTO.class);
    }

    @PostMapping(path="/banks")
    public BankDTO addBank(@RequestBody BankModifyDTO newBank)
    {
        return modelMapper.map(bankService.addBank(modelMapper.map(newBank, Bank.class)), 
        		               BankDTO.class);
    }

    @PutMapping(path="/banks/{id}")
    public BankDTO updateBank(@RequestBody BankModifyDTO updatedBank, @PathVariable Long id)
    {
        return modelMapper.map(bankService.updateBank(modelMapper.map(updatedBank, Bank.class), id), 
	               BankDTO.class);
    }

    @DeleteMapping(path="/banks/{id}")
    public void deleteBank(@PathVariable Long id)
    {
        bankService.deleteBank(id);
    }
}