package com.ib.ib.repository;

import com.ib.ib.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);
    User findUserByEmail(String email);
    Page<User> findAll(Pageable pageable);
    void deleteById(Integer id);
}
