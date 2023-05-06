package com.ib.ib.DTO;

import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.CertificateType;

public class CertificateRequestDTO {
    private Integer id;
    private String issuerSN;
    private CertificateType type;
    private long durationInMonths;
    private String captcha;

    public CertificateRequestDTO() {}
    public CertificateRequestDTO(CertificateRequest certificateRequest){
        this.id = certificateRequest.getId();
        this.issuerSN = "";
        if(certificateRequest.getIssuer() != null)
            this.issuerSN = certificateRequest.getIssuer().getId().toString();
        this.type = certificateRequest.getCertificateType();
        this.durationInMonths = certificateRequest.getDurationInMonths();
    }
    public CertificateRequestDTO(Integer id, String issuerSN, CertificateType type, Integer durationInMonths, String captcha) {
        this.id               = id;
        this.issuerSN         = issuerSN;
        this.type             = type;
        this.durationInMonths = durationInMonths;
        this.captcha = captcha;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getIssuerSN() {return issuerSN;}
    public void setIssuerSN(String issuerSN) {this.issuerSN = issuerSN;}

    public CertificateType getType() {return type;}
    public void setType(CertificateType type) {this.type = type;}

    public long getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(long durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "CertificateRequestDTO{" +
                "id=" + id +
                ", issuerSN='" + issuerSN + '\'' +
                ", type=" + type +
                ", durationInMonths=" + durationInMonths +
                '}';
    }
}
