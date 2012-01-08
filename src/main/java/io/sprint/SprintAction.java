package io.sprint;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class SprintAction {

	private String via;
	private Pattern route;
	private String conditions;
	private Method method;
	
	//private List<String> names = new ArrayList<String>();
	
	public SprintAction(String path, String method) {
		
		
		String[] parts = path.split("/");
		
		
		
		StringBuilder buf = new StringBuilder();
		
		for(int i = 1;i < parts.length;i++) {
			buf.append("/");
			if(parts[i].isEmpty()) {
				continue;
			} else if(parts[i].startsWith(":")) {
				//names.add(parts[i].substring(1));
				
				buf.append("(?");
				buf.append(parts[i].substring(1));
				buf.append("\\w+)");
			} else {
				buf.append(parts[i]);
			}
		}
		
		System.out.println(buf.toString());
		
		via = method.toLowerCase();
		route = Pattern.compile(buf.toString());
	}
	
	public boolean matches(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {

		if(!via.equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		
		Matcher matcher = route.matcher(request.getRequestURI());
		
		Map<String,String> uv = new HashMap<String,String>();
		if(matcher.matches()) {
			
			
			//matcher.
			if(matcher.groupCount() > 0) {
				for(int i = 0;i < matcher.groupCount();i++) {
					//System.out.println(names.get(i));
					//matcher.gr
					//uv.put(names.get(i), matcher.group(i));
				}
			}
			
			ObjectMapper mapper  = new ObjectMapper();
			
			System.out.println(mapper.writeValueAsString(uv));
			
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
	 * @param conditions the conditions to set
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}


	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	
	
	
}
