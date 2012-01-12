package io.trestle;

import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import jregex.MatchIterator;
import jregex.Matcher;
import jregex.Pattern;
import jregex.Replacer;


public class Action implements Comparable<Action> {

	private static final Pattern splat = new Pattern(":([a-zA-Z0-9:-_]+)");
	
	private String path;
	private String via;
	private Pattern route;
	private String conditions;
	private Method method;
	private Integer priority = 0;
	
	private LinkedList<String> names = new LinkedList<String>();
	
	public Action(String path, String via, Method method) {
		
		this.path = path;
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
		
		priority = (names.size() * 100) + path.length();
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

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the via
	 */
	public String getVia() {
		return via;
	}


	@Override
	public int compareTo(Action o) {
		return priority.compareTo(o.priority);
	}
}
