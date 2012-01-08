package io.sprint;

import io.sprint.annotation.Match;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;


public class SprintServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final ObjectMapper mapper = new ObjectMapper();
	private List<SprintAction> actions = new CopyOnWriteArrayList<SprintAction>(); 
	
	public void before(SprintContext context) {
	}
	
	public void after(SprintContext context) {
	}
	
	public void getDefault(SprintContext context) throws IOException {
		context.getResponse().sendError(404, "Resource not found");
	}
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		SprintContext context = new SprintContext(request, response);
		
		before(context);
		
		for(SprintAction action: actions) {
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
				
				if(mapper.canSerialize(result.getClass())) {
					mapper.writeValue(response.getOutputStream(), result);
					return;
				}
				
				System.out.println("Route found but unknown type");
			}
		}

		getDefault(context);
		after(context);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		for(Method m: getClass().getMethods()) {
			Match match = m.getAnnotation(Match.class);
			if(match == null) {
				continue;
			}
			actions.add(new SprintAction(match.value(), match.via()));
		}
	}
	
	protected Object status(int code) {
		return code;
	}
	
	protected Object status(int code, String message) {
		return code;
	}
	

}
