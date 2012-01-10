package io.trestle.assets;

public abstract class BundleProcessor {

	/**
	 * Process the entire concatenated file
	 * Useful for compression, validation, etc
	 */
	public String processBundle(String content) {
		return content;
	}
	
	/**
	 * Process an individual file.  Useful for wrapping
	 * the content with a function, or for wrapping
	 * template files with js variables.
	 */
	public String processFile(String path, String content) {
		return content;
	}
}
