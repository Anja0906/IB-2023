package com.ib.ib.model;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Objects;

public class SubjectData {
    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private LocalDate startDate;
    private LocalDate endDate;

    public SubjectData() {}
    public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, LocalDate startDate, LocalDate endDate) {
        this.publicKey      = publicKey;
        this.x500name       = x500name;
        this.serialNumber   = serialNumber;
        this.startDate      = startDate;
        this.endDate        = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectData that = (SubjectData) o;
        return publicKey.equals(that.publicKey) && x500name.equals(that.x500name) && serialNumber.equals(that.serialNumber) && startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, x500name, serialNumber, startDate, endDate);
    }

    public PublicKey getPublicKey() {return publicKey;}
    public void setPublicKey(PublicKey publicKey) {this.publicKey = publicKey;}

    public X500Name getX500name() {return x500name;}
    public void setX500name(X500Name x500name) {this.x500name = x500name;}

    public String getSerialNumber() {return serialNumber;}
    public void setSerialNumber(String serialNumber) {this.serialNumber = serialNumber;}

    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    @Override
    public String toString() {
        return "SubjectData{" +
                "publicKey=" + publicKey +
                ", x500name=" + x500name +
                ", serialNumber='" + serialNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
