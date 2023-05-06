package com.ib.ib.service;

import com.ib.ib.DTO.ReCaptchaAnswerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.validation.ValidationException;
import java.util.regex.Pattern;

@Service
public class CaptchaService {
    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Value("${google.recaptcha.key.site}")
    private String site;
    @Value("${google.recaptcha.key.secret}")
    private String secret;

    public void processResponse(String reCaptchaToken) {
        if (reCaptchaToken == null || reCaptchaToken.isEmpty() || !RESPONSE_PATTERN.matcher(reCaptchaToken).matches()) {
            throw new ValidationException("ReCaptcha token is invalid: " + reCaptchaToken);
        }

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("secret", secret);
        request.add("response", reCaptchaToken);
        RestTemplate restTemplate = new RestTemplate();
        ReCaptchaAnswerDTO result = restTemplate.postForObject(
                "https://www.google.com/recaptcha/api/siteverify",
                request,
                ReCaptchaAnswerDTO.class
        );

        if (result == null || !result.isSuccess()) {
            throw new ValidationException("ReCaptcha was not successfully validated: " + reCaptchaToken);
        }
    }
}
