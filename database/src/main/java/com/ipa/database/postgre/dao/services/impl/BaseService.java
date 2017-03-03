package com.ipa.database.postgre.dao.services.impl;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;


public class BaseService {
    @Autowired
    @Qualifier("postgreJdbc")
    protected @NonNull JdbcTemplate jdbcTemplate;
    protected @NonNull EntityManager em;
}
