package com.ib.ib.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchaAnswerDTO {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTimestamp;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private String[] errorCodes;

    // constructors, getters, and setters

    public ReCaptchaAnswerDTO() {}

    public ReCaptchaAnswerDTO(boolean success, String challengeTimestamp, String hostname, String[] errorCodes) {
        this.success = success;
        this.challengeTimestamp = challengeTimestamp;
        this.hostname = hostname;
        this.errorCodes = errorCodes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallengeTimestamp() {
        return challengeTimestamp;
    }

    public void setChallengeTimestamp(String challengeTimestamp) {
        this.challengeTimestamp = challengeTimestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }

}
