package com.ib.ib.repository;

import com.ib.ib.model.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.List;

public interface RequestRepository extends JpaRepository<CertificateRequest, Integer> {

    @Query("select c from CertificateRequest c inner join User u on c.issuedTo = u where u.id=:userId")
    List<CertificateRequest> findAllCertificateRequestsForUser(Integer userId);

    @Query("select u.id from CertificateRequest cr inner join Certificate c on cr.issuer=c " +
            "inner join User u on c.issuedTo.id=u.id where cr.id=:requestId")
    Long getIssuerCertificateUserIdByRequestId(Integer requestId);
}
