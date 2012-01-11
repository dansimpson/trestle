package io;

import io.trestle.assets.Bundle;
import io.trestle.assets.processors.LessProcessor;
import io.trestle.assets.processors.TemplateProcessor;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


public class BundleTest extends Base {

	@Test
	public void testBundleRegistration() {
		Bundle bundle = new Bundle("test");
		Assert.assertNotNull(Bundle.find("test"));
	}
	
	@Test
	public void testBundleFinding() throws IOException {
		Bundle bundle = new Bundle("test");
		
		bundle.setRoot("src/test/resources/tests");
		bundle.addPath("/deep/**/*.js");
		
		List<String> assets = bundle.getAssetList();

		Assert.assertEquals(3, assets.size());
		Assert.assertEquals("/deep/test.js", assets.get(0));
		Assert.assertEquals("!d\n!dd\n!ddd\n", bundle.process());
	}
	
	@Test
	public void testBundleFlattening() {
		Bundle bundle = new Bundle("test");
		
		bundle.setRoot("src/test/resources/tests");
		bundle.addPath("/test.js");
		bundle.addPath("/deep/test.js");
		bundle.addPath("/test.js");

		List<String> assets = bundle.getAssetList();

		Assert.assertEquals(2, assets.size());
		Assert.assertEquals("/test.js", assets.get(0));
	}
	
	@Test
	public void testTemplateBundle() throws IOException {
		Bundle bundle = new Bundle("test");
		
		bundle.setRoot("src/test/resources/tests");
		bundle.addPath("/test.jst");
		bundle.addProcessor(new TemplateProcessor("tpls"));

		Assert.assertEquals("tpls={};tpls[\"/test.jst\"]=\"content\";", bundle.process());
	}
	
	@Test
	public void testLessBundle() throws IOException {
		Bundle bundle = new Bundle("test");

		bundle.setRoot("src/test/resources/tests");
		bundle.addPath("/test.less");
		bundle.addProcessor(new LessProcessor(true));
		
		Assert.assertEquals("#header{color:#4d926f;}", bundle.process());
	}
	
	
}
