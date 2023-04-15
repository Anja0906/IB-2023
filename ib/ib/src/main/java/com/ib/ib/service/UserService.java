package com.ib.ib.service;

import com.ib.ib.model.User;
import com.ib.ib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserById(Integer id){return userRepository.findUserById(id);}
    public User findUserByEmail(String email){return userRepository.findUserByEmail(email);}
    public Page<User> findAll(Pageable pageable){return userRepository.findAll(pageable);}
    public void deleteById(Integer id){userRepository.deleteById(id);}

    public User getUserByPrincipal(OidcUser principal) {
        if (principal == null) return null;
        if (findUserByEmail(principal.getEmail()) == null) {
            User newUser = new User(principal.getEmail(), principal.getGivenName(), principal.getFamilyName(), principal.getPhoneNumber());
            userRepository.save(newUser);
        }
        return findUserByEmail(principal.getEmail());
    }
}
