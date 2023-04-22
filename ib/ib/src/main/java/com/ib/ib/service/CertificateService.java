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

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.*;

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

    public CertificateRequest createRequest(CertificateRequestDTO certificateRequestDTO, User issuedTo) throws Exception {
        Certificate issuer = null;
        issuedTo.setAdmin(true);
        System.out.println(issuedTo.IsAdministrator());
        if (!certificateRequestDTO.getIssuerSN().isEmpty()){
            System.out.println("1");
            issuer = this.certificateRepository.findCertificateByIssuerSerialNumber(certificateRequestDTO.getIssuerSN());
        }
        if (issuer != null){
            System.out.println("2");

            issuer.setIssuedTo(this.certificateRepository.getUserByCertificateId(issuer.getId()));
            validateIssuerEndCertificate(certificateRequestDTO, issuer);
        }

        CertificateRequest certificateRequest = new CertificateRequest(issuer, issuedTo, certificateRequestDTO.getType(), CertificateState.PENDING, "",certificateRequestDTO.getDurationInMonths());
        if(issuedTo.IsAdministrator()){
            if (issuer!=null){
                System.out.println("3");

                validateIssuer(certificateRequestDTO);
            }
            CertificateRequest newRequest = this.requestRepository.save(certificateRequest);
            if(certificateRequest.getIssuer() != null && issuedTo.getId().equals(certificateRequest.getIssuer().getIssuedTo().getId())) {
                System.out.println("4");

                this.acceptRequest(newRequest.getId(), issuedTo);
                newRequest.setStatus(CertificateState.ACCEPTED);
            }
            else if(certificateRequest.getIssuer() == null){
                System.out.println("5");

                this.acceptRequest(newRequest.getId(), issuedTo);
                newRequest.setStatus(CertificateState.ACCEPTED);
            }
            return newRequest;
        }
        if(certificateRequestDTO.getType().equals(CertificateType.ROOT)){
            System.out.println(certificateRequestDTO.getType());
            throw new Exception("Cannot create root certificate as a default user");
        }
        validateIssuer(certificateRequestDTO);
        certificateRequest.setCertificateType(certificateRequestDTO.getType());
        certificateRequest = this.requestRepository.save(certificateRequest);
        if (issuer != null && issuer.getIssuedTo().getId().equals(issuedTo.getId())) {
            System.out.println("6");

            this.acceptRequest(certificateRequest.getId(), issuedTo);
        }
        return certificateRequest;

    }

    private void validateIssuerEndCertificate(CertificateRequestDTO certificateRequest, Certificate issuer) throws Exception {
        if(certificateRequest.getIssuerSN().length() > 0) {
            if(issuer.getCertificateType().equals(CertificateType.END))
                throw new Exception("Type of issuer certificate cannot be end.");
        }
    }

    private void validateIssuer(CertificateRequestDTO certificateRequest) throws Exception {
        if(certificateRequest.getIssuerSN().isEmpty()){
            throw new Exception("Issuer cannot be null for intermediate or end certificates.");
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

    public void declineRequest(Integer id, String declineReason, User user) throws Exception {
        Integer userId = user.getId();
        Optional<CertificateRequest> request = this.requestRepository.findById(id);
        if(request.isEmpty()) throw new Exception("The request with the given id doesn't exist");

        if(!userId.equals(this.requestRepository.getIssuerCertificateUserIdByRequestId(request.get().getId()))){
            throw new Exception("The request with the given id doesn't exist");
        }

        if (request.get().getStatus()!=CertificateState.PENDING){
            throw new Exception("The request has already been processed");
        }

        request.get().setStatus(CertificateState.REJECTED);
        request.get().setReason(declineReason);
        this.requestRepository.save(request.get());
    }

    public String acceptRequest(Integer id, User issuedTo) throws Exception {
        Integer userId = issuedTo.getId();
        Optional<CertificateRequest> request = this.requestRepository.findById(id);
        if(request.isEmpty()) throw new Exception("The request with the given id doesn't exist");

        if (request.get().getCertificateType()!=CertificateType.ROOT){
            if (!Objects.equals(userId, this.requestRepository.getIssuerCertificateUserIdByRequestId(request.get().getId()))) {
                throw new Exception("The request with the given id doesn't exist");
            }
        }

        if (request.get().getStatus()!=CertificateState.PENDING){
            throw new Exception("The request has already been processed");
        }
        KeyPair keyPair = generateCertificateService.generateKeys();
        this.generateCertificateService.createCertificate(request.get(), keyPair);
        request.get().setStatus(CertificateState.ACCEPTED);
        this.requestRepository.save(request.get());
        return "Request accepted";
    }
}






