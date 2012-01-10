package io.trestle;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrestleRequest {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public TrestleRequest(HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.request = request;
		this.response = response;
	}
	
	public Map<String,String> params = new HashMap<String,String>();

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
		Object result = request.getAttribute(param);
		if(result != null) {
			return result.toString();
		}
		return request.getParameter(param);
	}
	
	public Integer getInt(String param) {
		return Integer.valueOf(get(param));
	}
	
	
}
