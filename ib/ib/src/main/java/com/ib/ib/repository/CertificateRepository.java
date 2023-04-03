package com.ib.ib.repository;

import com.ib.ib.model.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Certificate findCertificateById(Integer id);

    Page<Certificate> findAll(Pageable pageable);

    void deleteById(Integer id);
}
