package com.vop.tools.data;

/**
 * Represents a person
 * 
 * @author henri
 * 
 */
public class Person {

	private Integer id;
	private String name;
	private String phone;
	private String password;
	private String email;

	public Person(String name, String phone, String password, String email) {
		super();
		this.id = null;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.email = email;
	}

	public Person(Integer id, String name, String phone, String password,
			String email) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.password = password;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * saves person data to database
	 * @return true if successful
	 */
	public boolean save() {
		if (id == null) ; //make new user
		else ; // edit existing user
		return true;
	}
	
	/**
	 * load person data from database
	 * @param id
	 * @return
	 */
	public boolean load(int id) {
		this.id = id;
		return true;
	}
}
