package com.ib.ib.security;

import com.ib.ib.model.User;
import com.ib.ib.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulLoginEvent implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    UserService userService;

    @SneakyThrows
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        OidcUser userDetails = (OidcUser) event.getAuthentication().getPrincipal();
        User a = userService.getUserByPrincipal(userDetails);
        System.out.println(a.getEmail() + " logged in.");
    }
}
