package io.trestle;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;


public class TrestleAction {

	private String via;
	private Pattern route;
	private String conditions;
	private Method method;
	
	private LinkedList<String> names = new LinkedList<String>();
	
	public TrestleAction(String route, String via, Method method) {
		
		this.via = via.toLowerCase();
		this.method = method;

		if("".equals(route) || "/".equals(route)) {
			this.route = Pattern.compile("^/$");
		} else {
			
			String[] parts = route.split("/");
			StringBuilder buf = new StringBuilder();
			for(int i = 1;i < parts.length;i++) {
				buf.append("/");
				if(parts[i].isEmpty()) {
					continue;
				} else if(parts[i].startsWith(":")) {
					names.addLast(parts[i].substring(1));
					buf.append("([^/|$]+)");
				} else {
					buf.append(parts[i]);
				}
			}
			this.route = Pattern.compile(buf.toString());
		}
	}
	
	/**
	 * Match and mutate request
	 * @param request
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public boolean matches(HttpServletRequest request) {

		if(!via.equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		
		String path = request.getPathInfo();
		if(path == null) {
			path = "/";
		}
		
		Matcher matcher = route.matcher(path);

		// I am not exactly sure why groupCount is so broken...
		if(matcher.find()) {
			for(int i = 0;i < matcher.groupCount();i++) {
				request.setAttribute(names.get(i), matcher.group(i+1));
			}
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @return the conditions
	 */
	public String getConditions() {
		return conditions;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
}
