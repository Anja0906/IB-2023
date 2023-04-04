package com.ib.ib.service;

import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.DTO.CertificateRequestDTO;
import com.ib.ib.model.*;
import com.ib.ib.repository.CertificateRepository;
import com.ib.ib.repository.RequestRepository;
import com.ib.ib.repository.UserRepository;
import org.bouncycastle.cert.cmp.CertificateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    GenerateCertificateService generateCertificateService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    Certificate findCertificateById(Integer id){
        return certificateRepository.findCertificateById(id);
    };


    Page<Certificate> findAll(Pageable pageable){
        return certificateRepository.findAll(pageable);
    };

    void deleteById(Integer id){
        certificateRepository.deleteById(id);
    };

    public CertificateRequest createRequest(CertificateRequestDTO certificateRequestDTO) throws Exception {
        //ovo prepraviti nakon logina, umesto 1 da ide ulogovani id
        User issuedTo = userRepository.findUserById(Integer.getInteger("1"));
        Certificate issuer = null;
        if (!certificateRequestDTO.getIssuerSN().isEmpty()){
            issuer = this.certificateRepository.findCertificateByIssuerSerialNumber(certificateRequestDTO.getIssuerSN());
        }
        if (issuer != null){
            issuer.setIssuedTo(this.certificateRepository.getUserByCertificateId(issuer.getId()));
            validateIssuerEndCertificate(certificateRequestDTO, issuer);
        }
        CertificateRequest certificateRequest = new CertificateRequest(issuer, issuedTo, certificateRequestDTO.getType(), CertificateState.PENDING, "");
        requestRepository.save(certificateRequest);
        return certificateRequest;

    }

    private void validateIssuerEndCertificate(CertificateRequestDTO certificateRequest, Certificate issuer) throws Exception {
        if(certificateRequest.getIssuerSN().length() > 0) {
            if(issuer.getCertificateType().equals(CertificateType.END))
                throw new Exception("Type of issuer certificate cannot be end.");
        }
    }

    public List<CertificateDTO> getAll() {
        List<Certificate> certificates = this.certificateRepository.findAll();
        List<CertificateDTO> newCertificates = new ArrayList<>();
        for(Certificate certificate: certificates){
            newCertificates.add(new CertificateDTO(certificate));
        }
        return newCertificates;
    }

    public boolean isValid(Integer id) throws Exception {

        Optional<Certificate> certificate = certificateRepository.findById(id);
        if(certificate.isEmpty())
            throw new Exception();

        return !isExpired(certificate.get());
    }

    private boolean isExpired(Certificate certificate){
        return  certificate.getValidTo().isBefore(LocalDateTime.now());
    }


}






