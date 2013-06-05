package io.trestle.assets;

import io.trestle.assets.processors.BundleProcessor;
import io.trestle.assets.processors.ContentProcessor;
import io.trestle.assets.processors.FileProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a bundle of assets, such as javascript, javascript templates, coffescripts, CSS, LessCSS, etc.
 * 
 * @author dan
 * 
 */
public class Bundle implements Content {

	private static final Logger log = LoggerFactory.getLogger(Bundle.class);

	private String root = "";
	private final String name;
	private final String contentType;
	private String cache;

	private boolean cacheable = false;

	private Set<String> patterns = new LinkedHashSet<String>();
	private Set<ContentProcessor> processors = new LinkedHashSet<ContentProcessor>();

	public Bundle(String name, String contentType) {
		this.name = name;
		this.contentType = contentType;
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
	 *          the root to set
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
	 *          the patterns to set
	 */
	public void setPatterns(Set<String> patterns) {
		this.patterns = patterns;
	}

	public void addProcessor(ContentProcessor processor) {
		processors.add(processor);
	}

	/**
	 * Add a path to the file scanner
	 * 
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
	 * 
	 * @return
	 */
	public List<String> getAssetList() {
		List<String> assets = new ArrayList<String>();
		for (File asset : glob()) {
			assets.add(filename(asset.getPath()));
		}
		return assets;
	}

	/**
	 * Process the bundle, by collecting all files that match any of the given patterns, processing each of those with all processors. Then
	 * combining the result of each processed file, and processing the combined result.
	 * 
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
	 * 
	 * @throws IOException
	 */
	private String combine() throws IOException {
		StringBuilder buffer = new StringBuilder();

		for (File file : glob()) {

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

			buffer.append(process(filename(file.getPath()), new String(ostream.toByteArray(), "UTF8")));
			buffer.append("\n");
		}

		return buffer.toString().replaceAll("\r", "");
	}

	/**
	 * Process content for a single file
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	private String process(String path, String content) {
		String result = content;
		for (ContentProcessor p : processors) {
			if (p instanceof FileProcessor) {
				result = ((FileProcessor) p).process(path, result);
			}
		}
		return result;
	}

	/**
	 * Process combined content
	 * 
	 * @param content
	 * @return
	 */
	private String process(String content) {
		String result = content;
		for (ContentProcessor p : processors) {
			if (p instanceof BundleProcessor) {
				result = ((BundleProcessor) p).process(result);
			}
		}
		return result;
	}

	/**
	 * Strip off the root path
	 * 
	 * @param path
	 * @return
	 */
	private String filename(String path) {
		return path.replace('\\', '/').replaceFirst(root, "");
	}

	/**
	 * Find all files that match any patterns
	 * 
	 * @return
	 */
	private Set<File> glob() {
		Set<File> assets = new LinkedHashSet<File>();
		for (String glob : patterns) {
			assets.addAll(BundleUtil.eglob(root + "/" + glob));
		}
		return assets;
	}

	@Override
	public String getContent() {
		try {
			return process();
		} catch (IOException e) {
			log.debug("Error processing content for bundle {}", name);
		}
		return "";
	}

	@Override
	public String getContentType() {
		return contentType;
	}

}
