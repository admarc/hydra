package com.github.admarc.repository;

import com.github.admarc.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);
}
