package io.trestle.response;

import javax.servlet.http.HttpServletResponse;

public interface Response {
	public void apply(HttpServletResponse response);
}
