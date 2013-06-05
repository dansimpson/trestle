package io;

import com.fasterxml.jackson.annotation.JsonView;

public class TestPojo {

	public static class EmailView {
	}

	private int id;
	private String name;
	private String email;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the emailO
	 */
	@JsonView(EmailView.class)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *          the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
