package io.trestle.assets;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a bundle of assets, such as javascript, javascript templates,
 * coffescripts, CSS, LessCSS, etc.
 * 
 * @author dan
 * 
 */
public abstract class Bundle {

	private String root = "";
	private String name;
	private String cache;
	
	private boolean cacheable = false;
	private boolean compress = false;
	
	private Set<String> patterns = new LinkedHashSet<String>();
	private Set<BundleProcessor> processors = new LinkedHashSet<BundleProcessor>();

	public Bundle(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	

	
	
	
	
	
	/**
	 * 
	 * *****************************
	 * Static Helpers
	 * 
	 * *****************************
	 * 
	 */
	
	private static final ConcurrentHashMap<String, Bundle> map = new ConcurrentHashMap<String, Bundle>();

	/**
	 * Register a bundle by name
	 * 
	 * @param group
	 */
	public static void register(Bundle group) {
		map.put(group.getName(), group);
	}

	/**
	 * Development mode flag, disables compression and caching when set to true.
	 */
	public static boolean development = true;

	/**
	 * Find a bundle by name
	 * 
	 * @param name
	 *            the name of the satchel
	 * @return the satchel object
	 */
	public static Bundle find(String name) {
		return map.get(name);
	}

	/**
	 * Determine if a bundle exists
	 * 
	 * @param name
	 *            the name of the satchel
	 * @return true if the satchel exists
	 */
	public static boolean exists(String name) {
		return map.containsKey(name);
	}

}
