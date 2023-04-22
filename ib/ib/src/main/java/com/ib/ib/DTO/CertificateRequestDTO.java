package com.ib.ib.DTO;

import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.CertificateType;

public class CertificateRequestDTO {
    private Integer id;
    private String issuerSN;
    private CertificateType type;
    private Integer durationInMonths;

    public CertificateRequestDTO(CertificateRequest certificateRequest){
        this.id = certificateRequest.getId();
        if(certificateRequest.getIssuer() == null){
            this.issuerSN = "";
        }else{
            this.issuerSN = certificateRequest.getIssuer().getId().toString();
        }
        this.type = certificateRequest.getCertificateType();
        this.durationInMonths = certificateRequest.getDurationInMonths();
    }
    public CertificateRequestDTO() {}
    public CertificateRequestDTO(Integer id, String issuerSN, CertificateType type, Integer durationInMonths) {
        this.id         = id;
        this.issuerSN   = issuerSN;
        this.type       = type;
        this.durationInMonths = durationInMonths;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getIssuerSN() {return issuerSN;}
    public void setIssuerSN(String issuerSN) {this.issuerSN = issuerSN;}

    public CertificateType getType() {return type;}
    public void setType(CertificateType type) {this.type = type;}

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    @Override
    public String toString() {
        return "CertificateRequestDTO{" +
                "id=" + id +
                ", issuerSN='" + issuerSN + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
