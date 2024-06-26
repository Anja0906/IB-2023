package com.ib.ib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.DTO.CertificateRequestDTO;
import com.ib.ib.DTO.DeclineRequestDTO;
import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.User;
import com.ib.ib.model.*;
import com.ib.ib.service.CertificateService;
import com.ib.ib.service.GenerateCertificateService;
import com.ib.ib.service.RequestService;
import com.ib.ib.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/certificate")
public class CertificateController {
    @Autowired
    CertificateService certificateService;
    @Autowired
    GenerateCertificateService generateCertificateService;
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;

    public CertificateController(CertificateService certificateService, RequestService requestService, GenerateCertificateService generateCertificateService) {
        this.certificateService = certificateService;
        this.requestService     = requestService;
        this.generateCertificateService = generateCertificateService;
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<CertificateDTO>> getCertificates(@AuthenticationPrincipal Object principal) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        User user = userService.getUserByPrincipal(principal);
        List<CertificateDTO> allCertificates = this.certificateService.getAll();
        return new ResponseEntity<>(allCertificates, HttpStatus.OK);
    }

    @GetMapping(value="/download/{id}")
    @CrossOrigin
    public ResponseEntity<?> downloadCertificate(@AuthenticationPrincipal Object principal, @PathVariable("id")Integer id) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        User user = userService.getUserByPrincipal(principal);
        Certificate certificate = this.certificateService.findById(id);
        if (certificate.getIssuedTo().equals(user)){
            return new ResponseEntity<>(this.certificateService.download(certificate), HttpStatus.OK);
        } else if (certificate.getIssuer()!=null && certificate.getIssuer().getIssuedTo().equals(user)) {
            return new ResponseEntity<>(this.certificateService.download(certificate), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You have no authority for this certificate!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/downloadKey/{id}")
    @CrossOrigin
    public ResponseEntity<?> downloadKeyFile(@AuthenticationPrincipal Object principal, @PathVariable("id")Integer id) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        User user = userService.getUserByPrincipal(principal);
        Certificate certificate = this.certificateService.findById(id);
        if (certificate.getIssuedTo().equals(user)){
            return new ResponseEntity<>(this.certificateService.downloadKey(certificate), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You have no authority for this .key file!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/valid/{id}")
    @CrossOrigin
    public ResponseEntity<Boolean> getCertificateValidation(
            @PathVariable("id")Integer id) throws Exception {
        boolean isValid = this.certificateService.isValid(id);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/validUploaded")
    public ResponseEntity<?> checkIsValid(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.getContentType().equals("application/x-x509-ca-cert")) {
            return ResponseEntity.badRequest().body("Invalid file type");
        }
        byte[] bytes = file.getBytes();
        return new ResponseEntity<>(this.certificateService.validateByIssuerSN(bytes), HttpStatus.OK);
    }

    @GetMapping(value="/requests")
    @CrossOrigin
    public ResponseEntity<?> getAllCertificateRequestsForUser(@AuthenticationPrincipal Object principal) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        User user = userService.getUserByPrincipal(principal);
        List<CertificateRequest> certificateRequests = this.requestService.findAllCertificateRequestsForUser(user.getId());
        List<CertificateRequestDTO> certificateRequestDTOS = new ArrayList<>();
        for(CertificateRequest certReq: certificateRequests){
            certificateRequestDTOS.add(new CertificateRequestDTO(certReq));
        }
        return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
    }

    @GetMapping(value="/requests/all")
    @CrossOrigin
    public ResponseEntity<?> getAllCertificateRequestsForAdmin(@AuthenticationPrincipal Object principal) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        User user = userService.getUserByPrincipal(principal);
        if (user.getIsAdministrator()){
            List<CertificateRequest> certificateRequests = this.requestService.findAll();
            List<CertificateRequestDTO> certificateRequestDTOS = new ArrayList<>();
            for(CertificateRequest certReq: certificateRequests){
                certificateRequestDTOS.add(new CertificateRequestDTO(certReq));
            }
            return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Only admin can access this method", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/new")
    public ResponseEntity<?> createCertificate(@AuthenticationPrincipal Object principal, @RequestBody CertificateRequestDTO requestDTO) throws Exception {
        User user = userService.getUserByPrincipal(principal);
        if (!requestDTO.getType().equals(CertificateType.END) && !requestDTO.getType().equals(CertificateType.INTERMEDIATE) && !requestDTO.getType().equals(CertificateType.ROOT))
            return new ResponseEntity<>("Bad request body", HttpStatus.BAD_REQUEST);
        if (requestDTO.getType().equals(CertificateType.END) || requestDTO.getType().equals(CertificateType.INTERMEDIATE)){
            if(requestDTO.getIssuerSN().isEmpty()){
                return new ResponseEntity<>("Bad request body", HttpStatus.BAD_REQUEST);
            }
        }
        CertificateRequest newRequest = this.certificateService.createRequest(requestDTO, user);
        return new ResponseEntity<>(new CertificateRequestDTO(newRequest), HttpStatus.OK);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptCertificate(@AuthenticationPrincipal Object principal, @PathVariable("id")Integer id) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        User user = userService.getUserByPrincipal(principal);
        try {
            return new ResponseEntity<>(this.certificateService.accept(id, user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/decline/{id}")
    public ResponseEntity<?> declineCertificate(@AuthenticationPrincipal Object principal, @PathVariable("id")Integer id, @RequestBody DeclineRequestDTO declineRequestDTO) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        User user = userService.getUserByPrincipal(principal);
        try {
            this.certificateService.declineRequest(id, declineRequestDTO.getReason(), user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
