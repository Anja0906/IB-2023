package com.ib.ib.service;

import com.ib.ib.model.Certificate;
import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.User;
import com.ib.ib.repository.CertificateRepository;
import com.ib.ib.repository.UserRepository;

import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;

public class GenerateCertificateService {

    private static String certificatesDir = "certificates";

    private UserRepository userRepository;
    private CertificateRepository certificateRepository;


    private Certificate issuer;
    private User subject;
    private X509Certificate flags;
    private boolean isAuthority;
    private X509Certificate issuerCertificate;
    private LocalDateTime validTo;
    private RSAKey currentRSA;

    public GenerateCertificateService(UserRepository userRepository, CertificateRepository certificateRepository)
    {
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    public Certificate IssueCertificate (String IssuerSN, String subjectUsername, String keyUsageFlags, LocalDateTime validTo){

        return null;
    }


    private X509Certificate GenerateCertificate() throws NoSuchAlgorithmException, InvalidKeyException {
        String subjectText = String.format("CN=%s", subject.getEmail());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096); // Set key size to 4096 bits
        KeyPair keyPair = keyGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        currentRSA = (RSAKey) privateKey;


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//        CertificateRequest certificateRequest = new CertificateRequest(subjectText, currentRSA, MessageDigest.getInstance("SHA-256"))
//        var certificateRequest = new CertificateRequest(subjectText, currentRSA, HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
//
//        certificateRequest.CertificateExtensions.Add(new X509BasicConstraintsExtension(isAuthority, false, 0, true));
//        certificateRequest.CertificateExtensions.Add(new X509KeyUsageExtension(flags, false));
//
//        var generatedCertificate = issuerCertificate == null
//                ? certificateRequest.CreateSelfSigned(DateTime.Now, validTo)
//                : certificateRequest.Create(issuerCertificate, DateTime.Now, validTo,
//                Guid.NewGuid().ToByteArray());
        return null;
    }
}
