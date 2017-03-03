package com.ipa.database.postgre.dao.crud;

import com.ipa.database.postgre.dao.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String userName, String email);
}
