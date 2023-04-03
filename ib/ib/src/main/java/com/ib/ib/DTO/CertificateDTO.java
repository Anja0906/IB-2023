package com.ib.ib.DTO;

import com.ib.ib.model.Certificate;
import com.ib.ib.model.CertificateType;
import com.ib.ib.model.User;

import java.time.LocalDateTime;

public class CertificateDTO {
    private Integer id;

    private LocalDateTime issuedAt;

    private User user;

    private CertificateType type;

    private String serialNumber;

    private LocalDateTime validTo;

    public CertificateDTO() {
    }

    public CertificateDTO(Certificate certificate){
        this.id = certificate.getId();
        this.issuedAt = certificate.getValidFrom();
        this.user = certificate.getIssuedTo();
        this.type = certificate.getCertificateType();
        this.serialNumber = certificate.getSerialNumber();
        this.validTo = certificate.getValidTo();
    }

    public CertificateDTO(Integer id, LocalDateTime issuedAt, User user, CertificateType type, String serialNumber, LocalDateTime validTo) {
        this.id = id;
        this.issuedAt = issuedAt;
        this.user = user;
        this.type = type;
        this.serialNumber = serialNumber;
        this.validTo = validTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CertificateType getType() {
        return type;
    }

    public void setType(CertificateType type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "CertificateDTO{" +
                "id=" + id +
                ", issuedAt=" + issuedAt +
                ", user=" + user +
                ", type=" + type +
                ", serialNumber='" + serialNumber + '\'' +
                ", validTo=" + validTo +
                '}';
    }
}
