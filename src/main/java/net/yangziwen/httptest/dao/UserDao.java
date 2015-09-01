package net.yangziwen.httptest.dao;

import org.springframework.stereotype.Repository;

import net.yangziwen.httptest.dao.base.AbstractJdbcDaoImpl;
import net.yangziwen.httptest.model.User;

@Repository
public class UserDao extends AbstractJdbcDaoImpl<User> {}
