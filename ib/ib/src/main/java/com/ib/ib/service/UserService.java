package com.ib.ib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.ib.model.User;
import com.ib.ib.repository.UserRepository;
import jdk.jshell.spi.ExecutionControl;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserById(Integer id) {
        return userRepository.findUserById(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User getUserByPrincipal(Object principal) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        if (principal == null) return null;
        if (principal instanceof OidcUser) return getUserByOidcUser((OidcUser) principal);
        if (principal instanceof Jwt) return getUserByJwt((Jwt) principal);
        throw new ExecutionControl
                .NotImplementedException("This type of login is not yet supported. Use Oidc or Jwt instead.");
    }

    private User getUserByOidcUser(OidcUser principal) {
        if (principal == null) return null;
        if (findUserByEmail(principal.getEmail()) == null) {
            User newUser = new User(principal.getEmail(), principal.getGivenName(), principal.getFamilyName(), principal.getPhoneNumber(), false);
            // Side user can not be admin, those false
            userRepository.save(newUser);
        }
        return findUserByEmail(principal.getEmail());
    }

    private String getManagementApiToken() {
        /*
        This method is used to access the Auth0 as Admin. To access Auth0 as common user just use common user's token.
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", "okCV1iX8I6mb9BqQZ6YnNF1hDcx3fv5n");
        requestBody.put("client_secret", "OMfcJyR-DSJRRdd-L0AQQnxNeohhNfIv3_bOUTZgfVGQjKLFlHlKjh6k58hJnHmL");
        requestBody.put("audience", "https://dev-uox28mbzk3p270l1.us.auth0.com/api/v2/");
        requestBody.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, String> result = restTemplate.postForObject("https://dev-uox28mbzk3p270l1.us.auth0.com/oauth/token", request, HashMap.class);

        return result.get("access_token");
    }

    private String stringOrNullFromJackson(JsonNode root, String key) {
        JsonNode node = root.get(key);
        return (node != null && !node.isNull()) ? node.textValue() : null;
    }

    private User getUserByJwt(Jwt jwt) throws JsonProcessingException {
        if (jwt == null) return null;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://dev-uox28mbzk3p270l1.us.auth0.com/userinfo",
                HttpMethod.GET,
                entity,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        if (findUserByEmail(root.get("email").textValue()) == null) {
            User newUser = new User(
                    stringOrNullFromJackson(root, "email"),
                    stringOrNullFromJackson(root,"given_name"),
                    stringOrNullFromJackson(root,"family_name"),
                    stringOrNullFromJackson(root,"phone_number"), false);
            // Side user can not be admin, those false
            userRepository.save(newUser);
        }
        return findUserByEmail(root.get("email").textValue());
    }

}
