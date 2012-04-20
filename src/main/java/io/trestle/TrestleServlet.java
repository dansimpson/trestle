package io.trestle;

import io.trestle.annotation.Route;
import io.trestle.response.ErrorResponse;
import io.trestle.response.JsonResponse;
import io.trestle.response.RawResponse;
import io.trestle.response.Response;
import io.trestle.response.StatusResponse;
import io.trestle.response.StreamResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class TrestleServlet extends HttpServlet {

	private static final Logger log = LoggerFactory
			.getLogger(TrestleServlet.class);

	private List<Action> actions = new CopyOnWriteArrayList<Action>();

	public void before(Context context) {
		context.resp().setContentType("application/json");
	}

	public void after(Context context) {
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Context context = new Context(request, response);

		Response result = null;
		
		for (Action action : actions) {
			if (action.matches(request)) {

				before(context);
				try {
					result = (Response) action.getMethod()
							.invoke(this, context);
				} catch (Throwable t) {
					log.error(
							"Error invoking trestle Action for "
									+ request.getRequestURI(), t);

					result = new ErrorResponse(400, t.getMessage());
				}
				after(context);

				break;
			}
		}
		
		if(result == null) {
			result = new ErrorResponse(404, "Not found");
		}
		
		result.apply(response);
	}

	@Override
	public void init() throws ServletException {
		super.init();

		List<Action> tmp = new ArrayList<Action>();

		for (Method m : getClass().getMethods()) {
			Route match = m.getAnnotation(Route.class);
			if (match == null) {
				continue;
			}

			if (m.getReturnType() != Response.class
					|| m.getParameterTypes().length != 1
					|| m.getParameterTypes()[0] != Context.class) {
				log.error(
						"Route {} does not resemble public Response method(Context context);",
						match.match());
				continue;
			}

			tmp.add(new Action(match.match(), match.via(), m));
		}

		Collections.sort(tmp);

		actions.addAll(tmp);

		for (Action action : actions) {
			log.debug("Added route {} {}", action.getPath(), action.getVia());
		}
	}

	/**
	 * An empty response with a status code
	 * 
	 * @param code
	 * @return
	 */
	protected Response status(int code) {
		return new StatusResponse(code);
	}

	/**
	 * A simple error message with code and message
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	protected Response error(int code, String message) {
		return new ErrorResponse(code, message);
	}

	/**
	 * A json object response
	 * 
	 * @param object
	 * @return
	 */
	protected Response json(Object object, Class<?> view) {
		return new JsonResponse(object, view);
	}
	
	/**
	 * A json object response with view
	 * 
	 * @param object
	 * @return
	 */
	protected Response json(Object object) {
		return new JsonResponse(object);
	}

	/**
	 * A stream response
	 * 
	 * @param stream
	 * @return
	 */
	protected Response stream(InputStream stream) {
		return new StreamResponse(stream);
	}

	/**
	 * A byte array response
	 * 
	 * @param data
	 * @return
	 */
	protected Response raw(byte[] data) {
		return new RawResponse(data);
	}

	/**
	 * A string response
	 * 
	 * @param data
	 * @return
	 */
	protected Response raw(String data) {
		return new RawResponse(data.getBytes());
	}

}
