package io;

import java.io.IOException;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.junit.Assert;
import org.junit.Test;

public class ServletTest extends Base {

	private HttpTester put(String path, MultiMap<String> params) throws IOException, Exception {
		HttpTester request = new HttpTester();
		HttpTester response = new HttpTester();
		
		request.setMethod("PUT");
		request.setHeader("Host","tester");
		request.setURI(path);
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setVersion("HTTP/1.1");
		request.setContent(UrlEncoded.encode(params, "UTF8", false));
		
		response.parse(testServer.getResponses(request.generate()));
		
		return response;
	}
	
	private HttpTester get(String path) throws IOException, Exception {
		HttpTester request = new HttpTester();
		HttpTester response = new HttpTester();
		
		request.setMethod("GET");
		request.setHeader("Host","tester");
		request.setURI(path);
		request.setVersion("HTTP/1.1");
		 
		response.parse(testServer.getResponses(request.generate()));
		
		return response;
	}
	
	@Test
	public void testArray() throws IOException, Exception {
		HttpTester response = get("/tests/array");
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("[1,2,3]",response.getContent());
	}
	
	@Test
	public void testRoot() throws IOException, Exception {
		HttpTester response = get("/tests");
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testHash() throws IOException, Exception {
		HttpTester response = get("/tests/hash");
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("{\"name\":\"name\"}",response.getContent());
	}
	
	@Test
	public void testId() throws IOException, Exception {
		HttpTester response = get("/tests/35");
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("{\"id\":35,\"name\":\"name\",\"email\":\"email\"}", response.getContent());
	}
	
	@Test
	public void testFileExt() throws IOException, Exception {
		HttpTester response = get("/tests/jquery.js");
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("{\"file\":\"jquery\",\"ext\":\"js\"}", response.getContent());
	}
	
	
	@SuppressWarnings("serial")
	@Test
	public void testIdWithPut() throws IOException, Exception {
		HttpTester response = put("/tests/35", new MultiMap<String>() {{
			put("name","dan");
			put("email", "ds@moo.com");
		}});
		Assert.assertTrue(response.getMethod()==null);
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("{\"id\":35,\"name\":\"dan\",\"email\":\"ds@moo.com\"}", response.getContent());
	}
}
