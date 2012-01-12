package io;

import io.trestle.TrestleContext;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Trestle;

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

@SuppressWarnings("serial")
public class TestServlet extends TrestleServlet {
	
	@Trestle("/")
	public Object testRoot(TrestleContext context) {
		return status(200);
	}
	
	@Trestle("/test200")
	public Object test200(TrestleContext context) {
		return status(200);
	}
	
	@Trestle("/test404")
	public Object test404(TrestleContext context) {
		return status(404, "Test message");
	}
	
	@Trestle("/array")
	public Object list(TrestleContext context) {
		return new Integer[] { 1,2,3 };
	}
	
	@Trestle("/hash")
	public Object hash(TrestleContext context) {
		return new HashMap<String,String>() {{
			put("name","name");
		}};
	}
	
	@Trestle("/:id")
	public Object show(final TrestleContext context) {
		TestPojo result = new TestPojo();
		result.setEmail("email");
		result.setName("name");
		result.setId(context.getInt("id"));
		return result;
	}
	
	@Trestle("/:file.:ext")
	public Object filext(final TrestleContext context) {
		return new HashMap<String,String>() {{
			put("file",context.get("file"));
			put("ext",context.get("ext"));
		}};
	}
	
	@Trestle(value = "/:id", via = "put")
	public Object update(TrestleContext context) {
		TestPojo result = new TestPojo();
		result.setEmail(context.get("email"));
		result.setName(context.get("name"));
		result.setId(context.getInt("id"));
		return result;
	}
	
	@Trestle(value = "/:id/json", via = "put")
	public Object updateJson(TrestleContext context) {
		TestPojo pojo = context.read(TestPojo.class);
		return pojo;
	}

}
