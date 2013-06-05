package io.trestle.assets;

import io.trestle.Context;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Route;
import io.trestle.response.ContentResponse;
import io.trestle.response.Response;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class BundleServlet extends TrestleServlet {

	protected static final ConcurrentHashMap<String, Content> map = new ConcurrentHashMap<String, Content>();

	/**
	 * Register a bundle by name
	 * 
	 * @param group
	 */
	public static void registerBundle(String name, Content group) {
		map.put(name, group);
	}

	@Route(match = "/:bundle.:ext")
	public Response getBundle(Context context) throws IOException {
		String name = context.param("bundle");
		if (!map.contains(name)) {
			return error(404, "Not found");
		}
		return new ContentResponse(map.get(name));
	}

}