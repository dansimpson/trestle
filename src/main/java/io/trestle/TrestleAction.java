package io.trestle;

import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import jregex.MatchIterator;
import jregex.Matcher;
import jregex.Pattern;
import jregex.Replacer;


public class TrestleAction {

	private static final Pattern splat = new Pattern(":([a-zA-Z0-9:-_]+)");
	
	private String via;
	private Pattern route;
	private String conditions;
	private Method method;
	
	private LinkedList<String> names = new LinkedList<String>();
	
	public TrestleAction(String path, String via, Method method) {
		
		this.via = via.toLowerCase();
		this.method = method;

		if("".equals(path) || "/".equals(path)) {
			this.route = new Pattern("^/$");
		} else {

			String regex = path;
			
			Matcher m = splat.matcher(regex);
			MatchIterator itr = m.findAll();
			while(itr.hasMore()) {
				names.add(itr.nextMatch().group(1));
			}
			Replacer r = splat.replacer("([a-zA-Z0-9:-_]+)");

			this.route = new Pattern("^" + r.replace(regex) + "$");
		}
	}
	

	public boolean matches(HttpServletRequest request) {

		if(!via.equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		
		String path = request.getPathInfo();
		if(path == null) {
			path = "/";
		}
		
		Matcher matcher = route.matcher(path);
		if(matcher.matches()) {
			String[] groups = matcher.groups();
			for(int i = 1;i < groups.length;i++) {
				request.setAttribute(names.get(i-1), groups[i]);
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
