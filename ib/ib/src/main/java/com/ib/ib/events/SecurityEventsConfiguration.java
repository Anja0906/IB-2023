package com.ib.ib.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ib.ib.model.User;
import com.ib.ib.repository.UserRepository;
import com.ib.ib.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Configuration
public class SecurityEventsConfiguration {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        // His Auth0 password is "t", by the way
        userRepository.save(new User("t@t.ru", "t", "t", "+7", true));
    }

    @EventListener(AuthenticationSuccessEvent.class)
    public void onSuccessfulLogin(AuthenticationSuccessEvent event) throws ExecutionControl.NotImplementedException, JsonProcessingException {
        var user = userService.getUserByPrincipal(event.getAuthentication().getPrincipal());
        System.out.println(user.getEmail() + " logged in. IsAdmin = " + user.getIsAdministrator());
    }
}
