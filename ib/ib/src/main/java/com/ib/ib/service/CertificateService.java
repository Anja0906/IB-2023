package com.ib.ib.service;

import com.ib.ib.model.Certificate;
import com.ib.ib.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    Certificate findCertificateById(Integer id){
        return certificateRepository.findCertificateById(id);
    };

    Page<Certificate> findAll(Pageable pageable){
        return certificateRepository.findAll(pageable);
    };

    void deleteById(Integer id){
        certificateRepository.deleteById(id);
    };
}






