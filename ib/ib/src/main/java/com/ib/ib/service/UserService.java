package com.ib.ib.service;

import com.ib.ib.model.User;
import com.ib.ib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    User findUserById(Integer id){
        return userRepository.findUserById(id);
    };

    User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    };

    Page<User> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    };

    void deleteById(Integer id){
        userRepository.deleteById(id);
    };

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User loadUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
