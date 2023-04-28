package com.ib.ib.service;

import com.ib.ib.model.User;
import com.ib.ib.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    @Autowired
    PasswordRepository passwordRepository;

    User findByUserId(Integer id){return passwordRepository.findByUserId(id);}
}
