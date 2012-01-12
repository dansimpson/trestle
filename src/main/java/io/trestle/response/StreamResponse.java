package io.trestle.response;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamResponse implements Response {

	public static final Logger log = LoggerFactory
			.getLogger(StreamResponse.class);

	private InputStream istream;

	public StreamResponse(InputStream istream) {
		this.istream = istream;
	}

	@Override
	public void apply(HttpServletResponse response) {
		response.setStatus(200);
		try {
			OutputStream ostream = response.getOutputStream();

			byte[] buf = new byte[512];
			int numRead;
			while ((numRead = istream.read(buf)) >= 0) {
				ostream.write(buf, 0, numRead);
			}
			istream.close();
			ostream.close();

		} catch (Throwable t) {
			log.error("Unable to stream response...", t);
		}
	}

}
