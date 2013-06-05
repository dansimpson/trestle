package io;

import io.trestle.Context;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Route;
import io.trestle.response.Response;

import java.util.HashMap;

@SuppressWarnings("serial")
public class TestServlet extends TrestleServlet {

	@Route(match = "/")
	public Response testRoot(Context context) {
		return status(200);
	}

	@Route(match = "/test200")
	public Response test200(Context context) {
		return status(200);
	}

	@Route(match = "/test404")
	public Response test404(Context context) {
		return error(404, "Test message");
	}

	@Route(match = "/array")
	public Response list(Context context) {
		return json(new Integer[] { 1, 2, 3 });
	}

	@Route(match = "/hash")
	public Response hash(Context context) {
		return json(new HashMap<String, String>() {

			{
				put("name", "name");
			}
		});
	}

	@Route(match = "/:id")
	public Response show(final Context context) {
		TestPojo result = new TestPojo();
		result.setEmail("email");
		result.setName("name");
		result.setId(context.paramAsInt("id"));
		return json(result);
	}

	@Route(match = "/:id/view")
	public Response view(final Context context) {
		TestPojo result = new TestPojo();
		result.setEmail("email");
		result.setName("name");
		result.setId(context.paramAsInt("id"));
		return json(result, TestPojo.EmailView.class);
	}

	@Route(match = "/:file.:ext")
	public Response filext(final Context context) {
		return json(new HashMap<String, String>() {

			{
				put("file", context.param("file"));
				put("ext", context.param("ext"));
			}
		});
	}

	@Route(match = "/:id", via = "put")
	public Response update(Context context) {
		TestPojo result = new TestPojo();
		result.setEmail(context.param("email"));
		result.setName(context.param("name"));
		result.setId(context.paramAsInt("id"));
		return json(result);
	}

	@Route(match = "/:id/json", via = "put")
	public Response updateJson(Context context) {
		TestPojo pojo = context.read(TestPojo.class);
		return json(pojo);
	}

	@Route(match = "/stringy", via = "get")
	public Response stringy(Context context) {
		return raw("Hello!");
	}

	@Route(match = "/route/should/not/work", via = "get")
	public Response someBadMethod() {
		return raw("Hello!");
	}

	@Route(match = "/route/should/not/work2", via = "get")
	public Object someOtherBadMethod(Context context) {
		return raw("Hello!");
	}

	@Route(match = "/route/should/not/work3", via = "get")
	public void totallyFuckedMethod() {
	}

}
