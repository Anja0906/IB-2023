package com.ib.ib.controller;

import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.DTO.CertificateRequestDTO;
import com.ib.ib.model.Certificate;
import com.ib.ib.model.CertificateRequest;
import com.ib.ib.service.CertificateService;
import com.ib.ib.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/certificate")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    RequestService requestService;

    public CertificateController(CertificateService certificateService, RequestService requestService) {
        this.certificateService = certificateService;
        this.requestService     = requestService;
    }

    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getCertificates(){
        List<CertificateDTO> allCertificates = this.certificateService.getAll();
        return new ResponseEntity<>(allCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/valid/{id}")
    public ResponseEntity<Boolean> getCertificateValidation(
            @PathVariable("id")Integer id) throws Exception {

        //prepraviti nakon logina
        boolean isValid = this.certificateService.isValid(Integer.getInteger("1"));
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
    @GetMapping(value="/requests/overview/{id}")
    public ResponseEntity<?> getAllCertificateRequestsForUser(@PathVariable("id") Integer id){
        List<CertificateRequest> certificateRequests = this.requestService.findAllCertificateRequestsForUser(id);
        List<CertificateRequestDTO> certificateRequestDTOS = new ArrayList<>();
        for(CertificateRequest certReq: certificateRequests){
            certificateRequestDTOS.add(new CertificateRequestDTO(certReq));
        }
        return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
    }
}
