package com.ib.ib.DTO;

import com.ib.ib.model.CertificateRequest;
import com.ib.ib.model.CertificateState;
import com.ib.ib.model.CertificateType;

public class CertificateResponseDTO {
    private Integer             id;
    private String              issuerSN;
    private CertificateType     type;
    private long                durationInMonths;
    private CertificateState    certificateState;

    public CertificateResponseDTO() {}

    public CertificateResponseDTO(Integer id, String issuerSN, CertificateType type, long durationInMonths, CertificateState certificateState) {
        this.id                 = id;
        this.issuerSN           = issuerSN;
        this.type               = type;
        this.durationInMonths   = durationInMonths;
        this.certificateState   = certificateState;
    }

    public CertificateResponseDTO(CertificateRequest certificateRequest){
        this.id = certificateRequest.getId();
        this.issuerSN = "";
        if(certificateRequest.getIssuer() != null)
            this.issuerSN = certificateRequest.getIssuer().getId().toString();
        this.type = certificateRequest.getCertificateType();
        this.durationInMonths = certificateRequest.getDurationInMonths();
        this.certificateState = certificateRequest.getStatus();
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getIssuerSN() {return issuerSN;}

    public void setIssuerSN(String issuerSN) {this.issuerSN = issuerSN;}

    public CertificateType getType() {return type;}

    public void setType(CertificateType type) {this.type = type;}

    public long getDurationInMonths() {return durationInMonths;}

    public void setDurationInMonths(long durationInMonths) {this.durationInMonths = durationInMonths;}

    public CertificateState getCertificateState() {return certificateState;}

    public void setCertificateState(CertificateState certificateState) {this.certificateState = certificateState;}

    @Override
    public String toString() {
        return "CertificateResponseDTO{" +
                "id=" + id + '\'' +
                ", issuerSN='" + issuerSN + '\'' +
                ", type=" + type + '\'' +
                ", durationInMonths=" + durationInMonths + '\'' +
                ", certificateState=" + certificateState +
                '}';
    }
}