package io.trestle.response;

import io.trestle.assets.Content;

import javax.servlet.http.HttpServletResponse;

public class ContentResponse extends RawResponse {

	private Content content;

	public ContentResponse(Content content) {
		super(content.getContent().getBytes());
		this.content = content;
	}

	@Override
	public void apply(HttpServletResponse response) {
		response.setStatus(200);
		response.setContentType(content.getContentType());
		super.apply(response);
	}

}
