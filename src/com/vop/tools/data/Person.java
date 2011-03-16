package com.vop.tools.data;

/**
 * Represents a person
 * 
 * @author henri
 * 
 */
public class Person {

	private final Integer id;
	private String name;
	private String phone;
	private String password;
	private String email;

	public Person(Integer id, String name, String phone, String password,
			String email) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.email = email;
	}

	public Person(String name, String phone, String password, String email) {
		super();
		this.id = null;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}