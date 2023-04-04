package com.ib.ib.controller;

import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.service.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/certificate")
@AllArgsConstructor
public class CertificateController {
    @Autowired
    CertificateService certificateService;

    @GetMapping(value="/test")
    public ResponseEntity<String> test1() {
        return new ResponseEntity<String>("Yeah", HttpStatus.OK);
    }

    @GetMapping(value="/test-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> test2() {
        return new ResponseEntity<String>("Yeah, Admin!", HttpStatus.OK);
    }

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
