package com.ib.ib.DTO;

public class DeclineRequestDTO {
    private String reason;

    public DeclineRequestDTO() {}
    public DeclineRequestDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DeclineRequestDTO{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
