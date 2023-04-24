package com.ib.ib.controller;

import com.ib.ib.service.UserService;
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
public class HomeController {
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
        return  new ResponseEntity<>(userService.getUserApiToken(code, url), HttpStatus.OK);
    }
}
