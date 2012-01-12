package io.trestle.response;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonResponse implements Response {

	private static final Logger log = LoggerFactory.getLogger(JsonResponse.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
	}
	
	private Object data;
	
	public JsonResponse(Object data) {
		this.data = data;
	}
	
	
	@Override
	public void apply(HttpServletResponse response) {
		
		response.setContentType("application/json");
		response.setStatus(200);
		
		try {
			OutputStream ostream = response.getOutputStream();
			mapper.writeValue(ostream, data);
			ostream.flush();
			ostream.close();
		} catch (Throwable t) {
			log.error("Error serializing JSON object for response", t);
		}
	}

}
