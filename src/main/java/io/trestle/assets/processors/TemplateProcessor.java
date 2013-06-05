package io.trestle.assets.processors;

public class TemplateProcessor implements FileProcessor, BundleProcessor {

	private String name = "Tpls";

	public TemplateProcessor(String name) {
		this.name = name;
	}

	@Override
	public String process(String path, String content) {
		return name + "[\"" + path.replace('\\', '/') + "\"]=\"" + clean(content) + "\";";
	}

	@Override
	public String process(String content) {
		return name + "={};" + content;
	}

	private String clean(String content) {
		return content.replaceAll("\"", "\\\\\"").replaceAll(">\\s+<", "><").replaceAll("\n", "");
	}
}
