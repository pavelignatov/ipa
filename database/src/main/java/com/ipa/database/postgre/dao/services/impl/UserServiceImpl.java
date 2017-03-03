package com.ipa.database.postgre.dao.services.impl;

import com.ipa.database.postgre.dao.PostgreTransactions;
import com.ipa.database.postgre.dao.crud.UserRepository;
import com.ipa.database.postgre.dao.entities.User;
import com.ipa.database.postgre.dao.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends BaseService implements UserService {

    private final  @NonNull UserRepository userRepository;

    @Override
    @Transactional(value = PostgreTransactions.JPA, rollbackFor = Exception.class)
    public Long addUser(User user) {
        final User model = userRepository.save(user);
        log.trace("create new user name={}, email={}", user.getUsername(), user.getEmail());
        return model.getId();
    }

    @Override
    @Transactional(value = PostgreTransactions.JPA, rollbackFor = Exception.class)
    public Optional<User> findByUserNameOrEmail(@NonNull String userNameOrEmail) {
        Optional<User> model = userRepository.findByUsernameOrEmail(userNameOrEmail, userNameOrEmail);
        return model;
    }
}
