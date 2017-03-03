package com.ipa.services;

import com.google.common.collect.ImmutableMap;
import com.ipa.database.postgre.dao.PostgreTransactions;
import com.ipa.database.postgre.dao.entities.User;
import com.ipa.database.postgre.dao.entities.UserRole;
import com.ipa.database.postgre.dao.model.Roles;
import com.ipa.database.postgre.dao.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataInitializer {

    private final @NotNull UserService userService;
    private final @NotNull PasswordEncoder encoder;

    //fast init admin users
    private final Map<String, String> adminUsers = ImmutableMap.of(
                "root",  "baza4siskin",
                "admin", "batut4crazy"
            );

    @Transactional(value = PostgreTransactions.JPA, rollbackFor = Exception.class)
    public void initializeAdminUser() {
        for (Map.Entry<String, String> entry : adminUsers.entrySet()) {
            if (userService.findByUserNameOrEmail(entry.getKey()).isPresent()) {
                log.trace("user {} has already present", entry.getKey());
                continue;
            }

            User adminUser = User.builder()
                    .username(entry.getKey())
                    .password(encoder.encode(entry.getValue()))
                    .enabled(true).build();

            adminUser.getUserRole().add(UserRole.builder()
                    .user(adminUser)
                    .role(Roles.ROLE_ADMIN).build());

            userService.addUser(adminUser);
        }
    }
}
