package io.trestle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class TrestleContext {

	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
	}
	
	private String body;
	
	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	public TrestleContext(HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.req = request;
		this.resp = response;
	}
	
	public Map<String,String> params = new HashMap<String,String>();

	/**
	 * @return the request
	 */
	public HttpServletRequest req() {
		return req;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse resp() {
		return resp;
	}
	
	public String get(String param) {
		Object result = req.getAttribute(param);
		if(result != null) {
			return result.toString();
		}
		return req.getParameter(param);
	}
	
	public Integer getInt(String param) {
		return Integer.valueOf(get(param));
	}
	
	public String body() {
		
		if(body != null) {
			return body;
		}
		
		try {
			StringWriter writer = new StringWriter();
			BufferedReader reader = req.getReader();
			char[] buffer = new char[1024];
			int len;
			while ((len = reader.read(buffer, 0, buffer.length)) != -1) {
				writer.write(buffer, 0, len);
			}
			writer.close();
			body = writer.toString();
		} catch(Exception ex) {
			ex.printStackTrace();
			body = "";
		}
		
		return body;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> read() {
		return read(Map.class);
	}
	
	public <T> T read(Class<T> t) {
		try {
			return mapper.readValue(body(), t);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
