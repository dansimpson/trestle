package io.trestle.response;

import java.io.ByteArrayInputStream;

public class RawResponse extends StreamResponse {

	public RawResponse(byte[] bytes) {
		super(new ByteArrayInputStream(bytes));
	}
	
}
