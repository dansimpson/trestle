package io.trestle.assets.processors;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;

public class LessProcessor implements BundleProcessor {

	private LessEngine engine = new LessEngine();

	private boolean compress = false;

	public LessProcessor(boolean compress) {
		this.compress = compress;
	}

	@Override
	public String process(String content) {
		try {
			String result = engine.compile(content).replaceAll("\\\\n", "\n");
			if (compress) {
				result = result.replaceAll("\\s+", "");
			}
			return result;

		} catch (LessException e) {
			e.printStackTrace();
		}
		return content;
	}

}
