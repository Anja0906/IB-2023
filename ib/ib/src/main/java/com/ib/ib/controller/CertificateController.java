package com.ib.ib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.DTO.CertificateRequestDTO;
import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.User;
import com.ib.ib.service.CertificateService;
import com.ib.ib.service.RequestService;
import com.ib.ib.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/certificate")
public class CertificateController {
    @Autowired
    CertificateService certificateService;
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;

    public CertificateController(CertificateService certificateService, RequestService requestService) {
        this.certificateService = certificateService;
        this.requestService     = requestService;
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<CertificateDTO>> getCertificates(@AuthenticationPrincipal Object principal) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        User user = userService.getUserByPrincipal(principal);
        List<CertificateDTO> allCertificates = this.certificateService.getAll();
        return new ResponseEntity<>(allCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/valid/{id}")
    @CrossOrigin
    public ResponseEntity<Boolean> getCertificateValidation(
            @PathVariable("id")Integer id) throws Exception {

        //prepraviti nakon logina
        boolean isValid = this.certificateService.isValid(Integer.getInteger("1"));
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
    @GetMapping(value="/requests/overview/{id}")
    @CrossOrigin
    public ResponseEntity<?> getAllCertificateRequestsForUser(@PathVariable("id") Integer id, @AuthenticationPrincipal Object principal) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        User user = userService.getUserByPrincipal(principal);
        if (!user.getIsAdministrator()) {
            return new ResponseEntity<>("Only admin can access this method!", HttpStatus.FORBIDDEN);
        }

        List<CertificateRequest> certificateRequests = this.requestService.findAllCertificateRequestsForUser(id);
        List<CertificateRequestDTO> certificateRequestDTOS = new ArrayList<>();
        for(CertificateRequest certReq: certificateRequests){
            certificateRequestDTOS.add(new CertificateRequestDTO(certReq));
        }
        return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
    }
}
