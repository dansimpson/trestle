package io.trestle.assets.processors;

public interface FileProcessor extends ContentProcessor {

	public String process(String path, String content);
}
