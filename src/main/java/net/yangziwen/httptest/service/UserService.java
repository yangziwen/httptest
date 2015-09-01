package net.yangziwen.httptest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.yangziwen.httptest.dao.UserDao;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public User getUserById(long id) {
		return userDao.getById(id);
	}
	
	public User getUserByUsername(String username) {
		return userDao.first(new QueryParamMap().addParam("username", username));
	}
	
	public void saveOrUpdateUser(User user) {
		userDao.saveOrUpdate(user);
	}

}
