package com.ib.ib.controller;

import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.model.Certificate;
import com.ib.ib.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/certificate")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getCertificates(){
        List<CertificateDTO> allCertificates = this.certificateService.getAll();
        return new ResponseEntity<>(allCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/valid/{id}")
    public ResponseEntity<Boolean> getCertificateValidation(
            @PathVariable("id")Long id) throws Exception {

        //prepraviti nakon logina
        boolean isValid = this.certificateService.isValid(Integer.getInteger("1"));
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
}
