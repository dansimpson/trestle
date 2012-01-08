package io.sprint.example;

import io.sprint.SprintContext;
import io.sprint.SprintServlet;
import io.sprint.annotation.Match;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ExampleServlet extends SprintServlet {

	public static void main(String[] args) throws Exception {
		Server server = new Server(9000);
		ServletContextHandler root = new ServletContextHandler(ServletContextHandler.SESSIONS);

		  root.setContextPath("/");
		  root.setResourceBase("./");

		  server.setHandler(root);
		  
		  root.addServlet(new ServletHolder(new ExampleServlet()), "/*");
		  
		  server.start();
	}
	
	
	@Match("/test200")
	public Object test200(SprintContext context) {
		return status(200);
	}
	
	@Match("/test404")
	public Object test404(SprintContext context) {
		return status(404, "Test message");
	}
	
	@Match("/")
	public Object list(SprintContext context) {
		return new Integer[] { 1,2,3 };
	}
	
	@Match("/:id")
	public Object show(SprintContext context) {
		ExamplePojo result = new ExamplePojo();
		result.setEmail("email@email.com");
		result.setName("Test Name");
		result.setId(context.getInt("id"));
		return result;
	}
	
	@Match(value = "/:id", via = "put")
	public Object update(SprintContext context) {
		ExamplePojo result = new ExamplePojo();
		result.setEmail("email@email.com");
		result.setName("Test Name");
		result.setId(context.getInt("id"));
		return result;
	}

}
