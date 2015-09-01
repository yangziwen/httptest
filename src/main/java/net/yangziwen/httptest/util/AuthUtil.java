package net.yangziwen.httptest.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.SimpleHashRequest;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;

import net.yangziwen.httptest.model.User;

public class AuthUtil {
	
	private AuthUtil() {}
	
	public static Subject getCurrentSubject() {
		return SecurityUtils.getSubject();
	}
	
	public static boolean isAuthenticated() {
		return getCurrentSubject().isAuthenticated();
	}
	
	public static boolean isRemembered() {
		return getCurrentSubject().isRemembered();
	}
	
	public static boolean isUser() {
		return isAuthenticated() || isRemembered();
	}
	
	public static boolean isGuest() {
		return !isUser();
	}
	
	public static String getUsername() {
		return (String) getCurrentSubject().getPrincipal();
	}
	
	public static User getCurrentUser() {
		return getCurrentSubject().getPrincipals().oneByType(User.class);
	}
	
	public static Long getCurrentUserId() {
		User user = getCurrentUser();
		if(user == null) {
			return null;
		}
		return user.getId();
	}
	
	public static String hashPassword(String username, String password) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("Neither username nor password should be null!");
		}
		HashService hashService = new DefaultHashService();
		HashRequest hashRequest = new SimpleHashRequest("MD5", new SimpleByteSource(password), new SimpleByteSource(username), 3);
		return hashService.computeHash(hashRequest).toHex();
	}

}
