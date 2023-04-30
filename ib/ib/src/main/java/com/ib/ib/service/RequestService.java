package com.ib.ib.service;

import com.ib.ib.model.Certificate;
import com.ib.ib.model.CertificateRequest;
import com.ib.ib.repository.CertificateRepository;
import com.ib.ib.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RequestService {

    RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {this.requestRepository = requestRepository;}
    public List<CertificateRequest> findAllCertificateRequestsForUser(Integer id){
        return requestRepository.findAllCertificateRequestsForUser(id);
    }
    public List<CertificateRequest> findAll() {
        return requestRepository.findAll();
    }
}
