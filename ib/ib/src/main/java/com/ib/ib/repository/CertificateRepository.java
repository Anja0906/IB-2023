package com.ib.ib.repository;

import com.ib.ib.model.Certificate;
import com.ib.ib.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;


public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Optional<Certificate> findCertificateById(Integer id);
    Certificate findCertificateBySerialNumber(String serialNumber);
    @Query("select c.issuedTo from Certificate c where c.id=:id")
    User getUserByCertificateId(Integer id);
    @Query("select c from Certificate c where c.issuer.id=:id")
    ArrayList<Certificate> findIssuedCertificatesById(Integer id);
    Page<Certificate> findAll(Pageable pageable);
    void deleteById(Integer id);
}
