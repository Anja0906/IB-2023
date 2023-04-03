package com.ib.ib.repository;

import com.ib.ib.model.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.List;

public interface RequestRepository extends JpaRepository<CertificateRequest, Integer> {

    @Query("select c from CertificateRequest c inner join User u on c.issuedTo = u where u.id=:userId")
    List<CertificateRequest> findAllCertificateRequestsForUser(Integer userId);

}
