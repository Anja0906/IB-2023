package com.ib.ib.events;

import com.ib.ib.model.User;
import com.ib.ib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateDefaultUserOnStartup {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultUser() {
        System.out.println("We don't store users passwords, we store only hash codes");
        System.out.println("That's why we need to encode passwords of all predefined users");

        User user = new User(1000000, "alex@gmail.com", "alex", "mishutkin", "+797799", "root", true);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userService.save(user);
    }
}
