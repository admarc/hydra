package com.github.admarc.interactor.user;

import com.github.admarc.domain.User;
import com.github.admarc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUser {
    private UserRepository userRepository;

    GetUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(Long id) {
        return userRepository.findById(id);
    }
}
