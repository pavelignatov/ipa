package com.ipa.database.postgre.dao.services;

import com.ipa.database.postgre.dao.entities.User;

import java.util.Optional;

public interface UserService {
    Long addUser(User user);
    Optional<User> findByUserNameOrEmail(String userNameOrEmail);
}
