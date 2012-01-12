package io.trestle.response;

import javax.servlet.http.HttpServletResponse;

public class StatusResponse implements Response {

	private int code;
	
	public StatusResponse(int code) {
		this.code = code;
	}
	
	@Override
	public void apply(HttpServletResponse response) {
		response.setStatus(code);
	}

}
