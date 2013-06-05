package io.trestle.response;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorResponse implements Response {

	private static final Logger log = LoggerFactory.getLogger(ErrorResponse.class);

	private int code;
	private String message;

	public ErrorResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public void apply(HttpServletResponse response) {
		try {
			response.sendError(code, message);
		} catch (IOException e) {
			log.error("Unable to send error for ErrorResponse", e);
		}
	}

}
