package com.ib.ib.service;

import com.ib.ib.model.User;
import com.ib.ib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

}
