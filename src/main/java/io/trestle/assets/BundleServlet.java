package io.trestle.assets;

import io.trestle.Context;
import io.trestle.TrestleServlet;
import io.trestle.annotation.Route;
import io.trestle.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class BundleServlet extends TrestleServlet {

	private static final Map<String,String> mimes = new ConcurrentHashMap<String,String>() {{
		put("js","text/javascript");
		put("coffee","text/javascript");
		put("jst","text/javascript");
		put("mustache","text/javascript");
		put("css","text/css");
		put("less","text/css");
	}};
	
	@Route(match = "/:bundle.:ext")
	public Response getBundle(Context context) throws IOException {
		
		Bundle bundle = Bundle.find(context.param("bundle"));
		
		if(bundle != null) {
			String contentType = mimes.get(context.param("ext"));
			if(contentType == null) {
				contentType = "text/plain";
			}
			context.resp().setContentType(contentType);
			return raw(bundle.process());
		}

		return error(404, "Not found");
	}

}