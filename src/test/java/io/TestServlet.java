package io;

import io.trestle.TrestleRequest;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Trestle;

import java.util.HashMap;

@SuppressWarnings("serial")
public class TestServlet extends TrestleServlet {
	
	@Trestle("/")
	public Object testRoot(TrestleRequest context) {
		return status(200);
	}
	
	@Trestle("/test200")
	public Object test200(TrestleRequest context) {
		return status(200);
	}
	
	@Trestle("/test404")
	public Object test404(TrestleRequest context) {
		return status(404, "Test message");
	}
	
	@Trestle("/array")
	public Object list(TrestleRequest context) {
		return new Integer[] { 1,2,3 };
	}
	
	@Trestle("/hash")
	public Object hash(TrestleRequest context) {
		return new HashMap<String,String>() {{
			put("name","name");
		}};
	}
	
	@Trestle("/:id")
	public Object show(final TrestleRequest context) {
		TestPojo result = new TestPojo();
		result.setEmail("email");
		result.setName("name");
		result.setId(context.getInt("id"));
		return result;
	}
	
	@Trestle("/:file.:ext")
	public Object filext(final TrestleRequest context) {
		return new HashMap<String,String>() {{
			put("file",context.get("file"));
			put("ext",context.get("ext"));
		}};
	}
	
	@Trestle(value = "/:id", via = "put")
	public Object update(TrestleRequest context) {
		TestPojo result = new TestPojo();
		result.setEmail(context.get("email"));
		result.setName(context.get("name"));
		result.setId(context.getInt("id"));
		return result;
	}

}
