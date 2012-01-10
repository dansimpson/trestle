package io;

import org.eclipse.jetty.testing.ServletTester;
import org.junit.BeforeClass;

public class Base {
	
	protected static ServletTester testServer = new ServletTester();
	
	@BeforeClass
	public static void setup() throws Exception {
		testServer.setContextPath("/");
		testServer.addServlet(TestServlet.class, "/tests/*");
		testServer.start();
	}
}
