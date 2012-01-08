package io.sprint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SprintContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public SprintContext(HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public String get(String param) {
		return "";
	}
	
	public Integer getInt(String param) {
		return 1;
	}
	
	
}
