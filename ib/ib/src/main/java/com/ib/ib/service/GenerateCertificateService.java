package com.ib.ib.service;

import com.ib.ib.model.*;
import com.ib.ib.model.Certificate;
import com.ib.ib.repository.CertificateRepository;
import com.ib.ib.repository.UserRepository;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class GenerateCertificateService {

    private static String certificatesDir = "certificates";

    private UserRepository userRepository;
    private CertificateRepository certificateRepository;


    public GenerateCertificateService(UserRepository userRepository, CertificateRepository certificateRepository)
    {
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    private KeyPair generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(4096, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    Date.from(subjectData.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(subjectData.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData generateSubjectData(Certificate certificate) {
        KeyPair keyPairSubject = generateKeys();
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificate.getIssuedTo().getEmail());
        builder.addRDN(BCStyle.SURNAME, certificate.getIssuedTo().getLastName());
        builder.addRDN(BCStyle.GIVENNAME, certificate.getIssuedTo().getFirstName());
        builder.addRDN(BCStyle.UID, String.valueOf(certificate.getIssuedTo().getId()));
        return new SubjectData(keyPairSubject.getPublic(), builder.build(), certificate.getSerialNumber(), certificate.getValidFrom().toLocalDate(), certificate.getValidTo().toLocalDate());
    }

    public LocalDateTime getEndDate(Certificate issuer, CertificateType type) throws Exception {
        if (type == CertificateType.END){
            if(issuer.getValidTo().isBefore(LocalDateTime.now().plusMonths(3))) {
                throw new Exception();
            }
            return LocalDateTime.now().plusMonths(3);
        }
        else {
            if(issuer.getCertificateType() == type){
                if(issuer.getValidTo().minusMonths(1).isAfter(LocalDateTime.now()))
                    return issuer.getValidTo().minusMonths(1);
                else
                    throw new Exception();
            }
            if(issuer.getValidTo().isBefore(LocalDateTime.now().plusMonths(6))) {
                throw new Exception();            }
            return LocalDateTime.now().plusMonths(6);
        }
    }

    public Certificate createCertificate(CertificateRequest certificateRequest, KeyPair keyPair) throws Exception {
        LocalDateTime validTo = LocalDateTime.now();
        if(certificateRequest.getCertificateType() == CertificateType.ROOT){
            validTo.plusYears(5);
        }else{
            validTo = getEndDate(certificateRequest.getIssuer(),certificateRequest.getCertificateType());

        }

        Certificate certificate = new Certificate(UUID.randomUUID().toString(),certificateRequest.getIssuer(),
                "SHA256WithRSAEncryption",certificateRequest.getIssuedTo(),
                LocalDateTime.now(),validTo,
                keyPair.getPublic().toString(),"branislav",true,certificateRequest.getCertificateType());

        SubjectData subjectData = generateSubjectData(certificate);
        IssuerData issuerData  = generateIssuerData(keyPair.getPrivate(),certificate);
        X509Certificate newCertificate = generateCertificate(subjectData, issuerData);
        certificate = this.certificateRepository.save(certificate);
        saveCertificate(newCertificate);
        savePrivateKey(newCertificate, keyPair);
        return certificate;
    }

    private IssuerData generateIssuerData(PrivateKey issuerKey, Certificate certificate) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        if (certificate.getCertificateType()!=CertificateType.ROOT) {
            builder.addRDN(BCStyle.CN, certificate.getIssuedTo().getEmail());
            builder.addRDN(BCStyle.SURNAME, certificate.getIssuedTo().getLastName());
            builder.addRDN(BCStyle.GIVENNAME, certificate.getIssuedTo().getFirstName());
            builder.addRDN(BCStyle.UID, String.valueOf(certificate.getIssuedTo().getId()));
        }
        return new IssuerData(builder.build(),issuerKey);
    }

    private void savePrivateKey(X509Certificate certificate,KeyPair keyPair){
        try {
            JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("certificates/" + certificate.getSerialNumber() + ".key"));
            pemWriter.writeObject(keyPair.getPrivate());
            pemWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCertificate(X509Certificate certificate){
        try {
            X509CertificateHolder certHolder = new JcaX509CertificateHolder(certificate);
            FileOutputStream fos = new FileOutputStream("certificates/" + certificate.getSerialNumber() + ".crt");
            fos.write(certHolder.getEncoded());
            fos.close();
        } catch (CertificateEncodingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey(String certificateSN) {
        try {

            File keyFile = new File("certificates/" + new BigInteger(certificateSN.replace("-", ""), 16) + ".key");
            PEMParser pemParser = new PEMParser(new FileReader(keyFile));
            Object obj = pemParser.readObject();
            pemParser.close();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            PrivateKey privateKey = null;
            if (obj instanceof PEMKeyPair) {
                privateKey = converter.getPrivateKey(((PEMKeyPair) obj).getPrivateKeyInfo());
            } else if (obj instanceof PrivateKeyInfo) {
                privateKey = converter.getPrivateKey((PrivateKeyInfo) obj);
            }
            return privateKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
