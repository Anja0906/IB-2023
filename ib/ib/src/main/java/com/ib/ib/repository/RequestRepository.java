package com.ib.ib.repository;

import com.ib.ib.model.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<CertificateRequest, Integer> {

}
