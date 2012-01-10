package io.trestle;

import io.trestle.annotation.Trestle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
public class TrestleServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(TrestleServlet.class);
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private List<TrestleAction> actions = new CopyOnWriteArrayList<TrestleAction>(); 
	
	public void before(TrestleRequest context) {
		context.getResponse().setContentType("application/json");
	}
	
	public void after(TrestleRequest context) {
	}
	
	public void getDefault(TrestleRequest context) throws IOException {
		context.getResponse().sendError(404, "Resource not found");
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		TrestleRequest context = new TrestleRequest(request, response);
		
		before(context);
		
		for(TrestleAction action: actions) {
			if(action.matches(request)) {

				Object result = null;
				
				try {
					result = action.getMethod().invoke(this, context);
					
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				OutputStream ostream = response.getOutputStream();
				
				// Check class of result... look for streams etc to
				// do downloads or custom streaming
				if(result instanceof InputStream) {
					InputStream istream = (InputStream)result;
					int b;
					while ((b = istream.read()) != -1) {
						ostream.write(b);
					}
					return;
				} else if(result instanceof String) {
					ostream.write(((String)result).getBytes());
					return;
				} else if(mapper.canSerialize(result.getClass())) {
					mapper.writeValue(ostream, result);
					return;
				}
				
				log.debug("Route found but unknown type");
			}
		}

		getDefault(context);
		
		after(context);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		for(Method m: getClass().getMethods()) {
			Trestle match = m.getAnnotation(Trestle.class);
			if(match == null) {
				continue;
			}
			actions.add(new TrestleAction(match.value(), match.via(), m));
			
			log.info("Added route {} {}", match.via(), match.value());
		}
	}
	
	protected Object status(int code) {
		return code;
	}
	
	protected Object status(int code, String message) {
		return code;
	}
	

}
