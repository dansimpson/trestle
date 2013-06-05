package io.trestle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The context given to all actions.
 * 
 * @author dan
 * 
 */
public class Context {

	private static final ObjectMapper mapper = new ObjectMapper();

	static {

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	}

	private String body;

	private HttpServletRequest req;
	private HttpServletResponse resp;

	public Context(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.req = request;
		this.resp = response;
	}

	public Map<String, String> params = new HashMap<String, String>();

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

	/**
	 * Get a request param by name
	 * 
	 * @param name
	 *          the name of the paramter
	 * @return the value as a string or null
	 */
	public String param(String name) {
		Object result = req.getAttribute(name);
		if (result != null) {
			return result.toString();
		}
		return req.getParameter(name);
	}

	/**
	 * Get a request param as an integer
	 * 
	 * @param name
	 *          the name of the parameter
	 * @return the value
	 */
	public Integer paramAsInt(String name) {
		return Integer.valueOf(param(name));
	}

	/**
	 * Get a request param as a long
	 * 
	 * @param name
	 *          the name of the parameter
	 * @return the value
	 */
	public Long paramAsLong(String name) {
		return Long.valueOf(param(name));
	}

	/**
	 * Get a request param as a double
	 * 
	 * @param name
	 *          the name of the parameter
	 * @return the value
	 */
	public Double paramAsDouble(String name) {
		return Double.valueOf(param(name));
	}

	/**
	 * Get a request param as a float
	 * 
	 * @param name
	 *          the name of the parameter
	 * @return the value
	 */
	public Float paramAsFloat(String name) {
		return Float.valueOf(param(name));
	}

	/**
	 * Read the body of the request
	 * 
	 * @return
	 */
	public String body() {

		if (body != null) {
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
		} catch (Exception ex) {
			ex.printStackTrace();
			body = "";
		}

		return body;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Read the body into a map (assuming JSON content)
	 * @return
	 */
	public Map<String, String> read() {
		return read(Map.class);
	}

	/**
	 * Read the body and deserialize into a known type Assumes JSON content
	 * 
	 * @param t
	 *          the class to create
	 * @return an instance of t
	 */
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

	/**
	 * Read the body and deserialize into a known type Assumes JSON content
	 * 
	 * @param t
	 *          the class to create
	 * @return an instance of t
	 */
	public <T> T read(TypeReference<T> t) {
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
