package com.ipa.database.mysql.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MySqlService {
    @Autowired
    @Qualifier("jdbcUlmart")
    private @NonNull JdbcTemplate jdbcTemplate;
}
