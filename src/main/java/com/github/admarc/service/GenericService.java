package com.github.admarc.service;

import com.github.admarc.domain.RandomCity;
import com.github.admarc.domain.User;

import java.util.List;

public interface GenericService {
    User findByUsername(String username);

    List<User> findAllUsers();

    List<RandomCity> findAllRandomCities();
}
