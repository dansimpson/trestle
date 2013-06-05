package io.trestle.response;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

public class HtmlResponse extends StreamResponse {

	public HtmlResponse(File file) throws FileNotFoundException {
		super(new FileInputStream(file));
	}

	public HtmlResponse(String content) {
		super(new ByteArrayInputStream(content.getBytes()));
	}

	@Override
	public void apply(HttpServletResponse response) {
		response.setContentType("text/html");
		super.apply(response);
	}
}
