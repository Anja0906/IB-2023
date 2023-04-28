package com.ib.ib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ib.ib.model.User;
import com.ib.ib.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for the home page.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("profile", principal.getClaims());
        }
        return "index";
    }

    @PostMapping("/front/login")
    @CrossOrigin
    public ResponseEntity<?> returnToken(@RequestBody Map<String, Object> requestBody) {
        String code = (String) requestBody.get("code");
        String url = (String) requestBody.get("url");
        return new ResponseEntity<>(userService.getUserApiToken(code, url), HttpStatus.OK);
    }

    @GetMapping("/api/user")
    @CrossOrigin
    public ResponseEntity<?> returnToken(@AuthenticationPrincipal Object principal) throws JsonProcessingException, ExecutionControl.NotImplementedException {
        User user = userService.getUserByPrincipal(principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
