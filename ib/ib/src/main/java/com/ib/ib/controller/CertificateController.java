package com.ib.ib.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certificate")
@AllArgsConstructor
public class CertificateController {
    @GetMapping(value="/test")
    public ResponseEntity<String> test1() {
        return new ResponseEntity<String>("Yeah", HttpStatus.OK);
    }

    @GetMapping(value="/test-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> test2() {
        return new ResponseEntity<String>("Yeah, Admin!", HttpStatus.OK);
    }
}
