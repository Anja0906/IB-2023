package com.ib.ib.repository;

import com.ib.ib.model.Password;
import com.ib.ib.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Integer> {

    User findByUserId(Integer id);



}
