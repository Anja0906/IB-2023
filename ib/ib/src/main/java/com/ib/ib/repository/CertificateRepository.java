package com.ib.ib.repository;

import com.ib.ib.model.Certificate;
import com.ib.ib.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Certificate findCertificateById(Integer id);

    Certificate findCertificateByIssuerSerialNumber(String serialNumber);

    @Query("select c.issuedTo from Certificate c where c.id=:id")
    User getUserByCertificateId(Integer id);

    Page<Certificate> findAll(Pageable pageable);

    void deleteById(Integer id);

    HashSet<Certificate> findAllCertificatesForUser(Integer id);
}
