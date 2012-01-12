package io.trestle.assets;

import io.trestle.assets.processors.BundleProcessor;
import io.trestle.assets.processors.ContentProcessor;
import io.trestle.assets.processors.FileProcessor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Represents a bundle of assets, such as javascript, javascript templates,
 * coffescripts, CSS, LessCSS, etc.
 * 
 * @author dan
 * 
 */
public class Bundle {

	private String root = "";
	private String name;
	private String cache;

	private boolean cacheable = false;

	private Set<String> patterns = new LinkedHashSet<String>();
	private Set<ContentProcessor> processors = new LinkedHashSet<ContentProcessor>();

	public Bundle(String name) {
		this.name = name;
		register(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 * @return the patterns
	 */
	public Set<String> getPatterns() {
		return patterns;
	}

	/**
	 * @param patterns
	 *            the patterns to set
	 */
	public void setPatterns(Set<String> patterns) {
		this.patterns = patterns;
	}

	public void addProcessor(ContentProcessor processor) {
		processors.add(processor);
	}
	
	/**
	 * Add a path to the file scanner
	 * @param glob
	 */
	public void addPath(String glob) {
		patterns.add(glob);
	}
	
	/**
	 * Enable caching for the bundle
	 */
	public void enableCaching() {
		cacheable = true;
	}
	
	/**
	 * Disable caching for the bundle
	 */
	public void disableCaching() {
		cacheable = false;
		cache = null;
	}
	
	/**
	 * Clear the cache
	 */
	public void clearCache() {
		cache = null;
	}

	/**
	 * Get a list of all assets found for this bundle
	 * @return
	 */
	public List<String> getAssetList() {
		List<String> assets = new ArrayList<String>();
		for (String asset : glob()) {
			assets.add(filename(asset));
		}
		return assets;
	}
	
	/**
	 * Process the bundle, by collecting all files
	 * that match any of the given patterns, processing
	 * each of those with all processors.  Then combining the
	 * result of each processed file, and processing the combined
	 * result.
	 * @return
	 * @throws IOException
	 */
	public String process() throws IOException {
	    if (cacheable) {
	      if (cache == null) {
	    	  cache = process(combine());
	      }
	      return cache;
	    }
	    return process(combine());
	}

	/**
	 * Concatenate all files, processing each individually if needed
	 * @throws IOException 
	 */
	private String combine() throws IOException {
		StringBuilder buffer = new StringBuilder();
		
		for (String file : glob()) {
			
			FileInputStream istream = new FileInputStream(file);
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();

			try {
				int b;
				while ((b = istream.read()) != -1) {
					ostream.write(b);
				}
				ostream.flush();
			} finally {
				istream.close();
				ostream.close();
			}
			
			buffer.append(process(filename(file), new String(ostream.toByteArray(), "UTF8")));
		}

		return buffer.toString();
	}

	/**
	 * Process content for a single file
	 * @param path
	 * @param content
	 * @return
	 */
	private String process(String path, String content) {
		String result = content;
		for(ContentProcessor p: processors) {
			if(p instanceof FileProcessor) {
				result = ((FileProcessor)p).process(path, result);
			}
		}
		return result;
	}
	
	/**
	 * Process combined content
	 * @param content
	 * @return
	 */
	private String process(String content) {
		String result = content;
		for(ContentProcessor p: processors) {
			if(p instanceof BundleProcessor) {
				result = ((BundleProcessor)p).process(result);
			}
		}
		return result;
	}
	
	/**
	 * Strip off the root path
	 * @param path
	 * @return
	 */
	private String filename(String path) {
		return path.replace('\\', '/').replaceFirst(root, "");
	}

	/**
	 * Find all files that match any patterns
	 * @return
	 */
	private Set<String> glob() {
		Set<String> assets = new LinkedHashSet<String>();
		for (String glob : patterns) {
			assets.addAll(BundleUtil.eglob(root + "/" + glob));
		}
		return assets;
	}

	/**
	 * 
	 * ***************************** Static Helpers
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
