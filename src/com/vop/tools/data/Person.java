package com.vop.tools.data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a Person
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

	/**
	 * Sets a new password for this Person. It is automatically hashed using
	 * SHA-1.
	 * 
	 * @param password
	 *            Plain text password
	 */
	public void setPassword(String password) {
		this.password = hashString(password);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Hash a string using SHA-1
	 * 
	 * @param clearText
	 * @return hashed string
	 */
	private static String hashString(String clearText) {
		try {
			MessageDigest m = MessageDigest.getInstance("SHA-1");
			m.update(clearText.getBytes(), 0, clearText.length());
			clearText = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
		}

		return clearText;
	}
}