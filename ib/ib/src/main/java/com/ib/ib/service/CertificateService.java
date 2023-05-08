package com.ib.ib.service;

import com.ib.ib.DTO.CertificateDTO;
import com.ib.ib.DTO.CertificateRequestDTO;
import com.ib.ib.exceptions.*;
import com.ib.ib.model.*;
import com.ib.ib.repository.CertificateRepository;
import com.ib.ib.repository.RequestRepository;
import com.ib.ib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class
CertificateService {
    private static String certificatesDir = "certificates/";

    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    GenerateCertificateService generateCertificateService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    public CertificateService(CertificateRepository certificateRepository, GenerateCertificateService generateCertificateService, UserRepository userRepository, RequestRepository requestRepository) {
        this.certificateRepository          = certificateRepository;
        this.generateCertificateService     = generateCertificateService;
        this.userRepository                 = userRepository;
        this.requestRepository              = requestRepository;
    }

    public Certificate findCertificateById(Integer id){
        return certificateRepository.findCertificateById(id).orElseThrow(() -> new CertificateNotFoundException("Certificate by id" + id + "was not found"));
    }
    public Page<Certificate> findAll(Pageable pageable){
        return certificateRepository.findAll(pageable);
    }
    public void deleteById(Integer id){
        certificateRepository.deleteById(id);
    }

    public List<CertificateDTO> getAll() {
        List<Certificate> certificates = this.certificateRepository.findAll();
        List<CertificateDTO> newCertificates = new ArrayList<>();
        for(Certificate certificate: certificates)
            newCertificates.add(new CertificateDTO(certificate));
        return newCertificates;
    }

    public List<CertificateDTO> getAllValid() {
        List<Certificate> certificates = this.certificateRepository.findAll();
        List<CertificateDTO> newCertificates = new ArrayList<>();
        for(Certificate certificate: certificates){
            if (certificate.isValid() && certificate.getCertificateType()!=CertificateType.END
                    && !this.isExpired(certificate)){
                newCertificates.add(new CertificateDTO(certificate));
            }
        }
        return newCertificates;
    }

    public CertificateRequest createRequest(CertificateRequestDTO certificateRequestDTO, User issuedTo) throws Exception {
        Certificate issuer = null;
        if (!certificateRequestDTO.getIssuerSN().isEmpty())
            issuer = this.certificateRepository.findCertificateBySerialNumber(certificateRequestDTO.getIssuerSN());

        if (issuer != null){
            issuer.setIssuedTo(this.certificateRepository.getUserByCertificateId(issuer.getId()));
            validateIssuerEndCertificate(issuer);
        }

        CertificateRequest request = new CertificateRequest();
        request.setIssuedTo(this.userRepository.findById(issuedTo.getId()).get());
        request.setStatus(CertificateState.PENDING);
        request.setIssuer(issuer);
        request.setDurationInMonths(certificateRequestDTO.getDurationInMonths());

        if(issuedTo.getIsAdministrator()){
            if (issuer!=null)
                validateIssuer(certificateRequestDTO);

            request.setCertificateType(certificateRequestDTO.getType());
            CertificateRequest newRequest = this.requestRepository.save(request);

            if(request.getIssuer() != null && issuedTo.getId().equals(request.getIssuer().getIssuedTo().getId())) {
                this.acceptRequest(newRequest.getId(), issuedTo);
                newRequest.setStatus(CertificateState.ACCEPTED);
            }

            else if(request.getIssuer() == null){
                this.acceptRequest(newRequest.getId(), issuedTo);
                newRequest.setStatus(CertificateState.ACCEPTED);
            }

            return newRequest;
        }
        if(certificateRequestDTO.getType().equals(CertificateType.ROOT))
            throw new Exception("Cannot create root certificate as a default user");
        validateIssuer(certificateRequestDTO);
        request.setCertificateType(certificateRequestDTO.getType());
        CertificateRequest newRequest = this.requestRepository.save(request);
        if(issuer.getIssuedTo().getId().equals(issuedTo.getId()))
            this.acceptRequest(newRequest.getId(), issuedTo);
        return newRequest;
    }

    private void validateIssuerEndCertificate(Certificate issuer) throws Exception {
        if(issuer.getCertificateType().equals(CertificateType.END))
            throw new Exception("Type of issuer certificate cannot be end.");
    }
    private void validateIssuer(CertificateRequestDTO certificateRequest) throws IssuerCertificateIsEndException {
        if(certificateRequest.getIssuerSN().equals(""))
            throw new IssuerCertificateIsEndException("Issuer cannot be null for intermediate or end certificates.");
    }
    public boolean isValid(Integer id) throws CertificateNotFoundException {
        try{
            Optional<Certificate> certificate = certificateRepository.findCertificateById(id);
            return !isExpired(certificate.get());
        }catch(CertificateNotFoundException exception){
            return false;
        }
    }
    private boolean isExpired(Certificate certificate){
        return  certificate.getValidTo().isBefore(LocalDateTime.now()) || !certificate.isValid();
    }

    public void declineRequest(Integer id, String declineReason, User user) throws RequestNotFoundException, RequestAlreadyProcessedException {
        Integer userId = user.getId();
        Optional<CertificateRequest> request = this.requestRepository.findById(id);
        if(request.isEmpty()) throw new RequestNotFoundException("The request with the given id doesn't exist");

        if(!userId.equals(this.requestRepository.getIssuerCertificateUserIdByRequestId(request.get().getId())))
            throw new RequestNotFoundException("The request with the given id doesn't exist");

        if (request.get().getStatus()!=CertificateState.PENDING)
            throw new RequestAlreadyProcessedException("The request has already been processed");

        request.get().setStatus(CertificateState.REJECTED);
        request.get().setReason(declineReason);
        this.requestRepository.save(request.get());
    }

    public String acceptRequest(Integer id, User issuedTo) throws Exception {
        Optional<CertificateRequest> request = this.requestRepository.findById(id);
        if(request.isEmpty()) throw new RequestNotFoundException("The request with the given id doesn't exist");

        if (request.get().getCertificateType()!=CertificateType.ROOT)
            if (!issuedTo.getId().equals(request.get().getIssuedTo().getId()))
                throw new RequestNotFoundException("The request with the given id doesn't exist");

        if (request.get().getStatus()!=CertificateState.PENDING)
            throw new RequestAlreadyProcessedException("The request has already been processed");

        KeyPair keyPair = generateCertificateService.generateKeys();
        this.generateCertificateService.createCertificate(request.get(), keyPair);
        request.get().setStatus(CertificateState.ACCEPTED);
        this.requestRepository.save(request.get());
        return "Request accepted";
    }


    public String accept(Integer id, User issuer) throws Exception {
        Optional<CertificateRequest> request = this.requestRepository.findById(id);
        if(request.isEmpty()) throw new RequestNotFoundException("The request with the given id doesn't exist");

        if(issuer.equals(request.get().getIssuer().getIssuedTo())){
            if (request.get().getStatus()!=CertificateState.PENDING)
                throw new RequestAlreadyProcessedException("The request has already been processed");
            KeyPair keyPair = generateCertificateService.generateKeys();
            this.generateCertificateService.createCertificate(request.get(), keyPair);
            request.get().setStatus(CertificateState.ACCEPTED);
            this.requestRepository.save(request.get());
            return "Request accepted";
        }
        else{
            throw new AuthorityException("You have no authorities for this request!");
        }
    }

    public Resource download(Certificate certificate,String type){
        String serialNumber = certificate.getSerialNumber();
        return new FileSystemResource(certificatesDir + new BigInteger(serialNumber.replace("-", ""), 16) + "."+type);
    }

    BigInteger convertToX509Certificate(byte[] certificateBytes) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certificateBytes);
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
        return x509Certificate.getSerialNumber();
    }

    public boolean validateByIssuerSN(byte[] bytes) throws Exception {
        BigInteger issuerSNBigInt = this.convertToX509Certificate(bytes);
        Certificate certificate = null;
        List<Certificate> certificates = this.certificateRepository.findAll();
        for (Certificate cert:certificates)
            if (issuerSNBigInt.equals(new BigInteger(cert.getSerialNumber().replace("-", ""), 16)))
                certificate = cert;
        if (certificate==null)
            return false;

        return certificate.isValid();
    }
    public void deleteCertificate(Certificate certificate) {
        certificate.setValid(false);
        certificateRepository.save(certificate);
        List <Certificate> issuedCertificates = certificateRepository.findIssuedCertificatesById(certificate.getId());
        if(!issuedCertificates.isEmpty())
            for(Certificate c: issuedCertificates)
                deleteCertificate(c);
    }
}






