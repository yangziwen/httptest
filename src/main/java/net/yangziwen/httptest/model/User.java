package net.yangziwen.httptest.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import net.yangziwen.httptest.model.base.AbstractModel;

@Table(name = "user")
public class User extends AbstractModel {

	@Id
	@Column
	private long id;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@Column
	private boolean enabled;
	
	public User() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
