package com.nectux.mizan.hyban.personnel.specifque.web;

import com.nectux.mizan.hyban.personnel.specifque.dto.SpecialContractDTO;
import com.nectux.mizan.hyban.personnel.specifque.entity.SpecialContract;
import com.nectux.mizan.hyban.personnel.specifque.services.SpecialContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnels/specifique")
public class SpecialContractController {

    private final SpecialContractService service;

    public SpecialContractController(SpecialContractService service) {
        this.service = service;
    }

    @PostMapping
    public SpecialContract create(@RequestBody SpecialContract contract) {
        return service.save(contract);
    }

    @GetMapping("/employee/{employeeId}")
    public SpecialContractDTO byEmployee(@PathVariable Long employeeId) {
        SpecialContractDTO specialContractDTO=new SpecialContractDTO();
        specialContractDTO.setRow(service.findByEmployee(employeeId).get(0));
        specialContractDTO.setRows(service.findByEmployee(employeeId));
        specialContractDTO.setResult("success");
        specialContractDTO.setStatus(true);
        return specialContractDTO;
    }
}
