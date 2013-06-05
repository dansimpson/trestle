package io;

import io.trestle.assets.Bundle;
import io.trestle.assets.processors.CoffeeProcessor;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

public class CoffeeTest extends Base {

	private static final CoffeeProcessor processor = new CoffeeProcessor();

	@Test
	public void testCompile() throws IOException, ScriptException, NoSuchMethodException {
		Assert.assertEquals("(function(){}).call(this);", processor.process("").replaceAll("\\s", ""));
	}

	@Test
	public void testCompile2() throws IOException, ScriptException, NoSuchMethodException {
		Assert.assertEquals("(function(){varx;x=function(){return2;};}).call(this);", processor.process("x = () -> 2").replaceAll("\\s", ""));
	}

	@Test
	public void testBundle() throws IOException, ScriptException, NoSuchMethodException {
		Bundle bundle = new Bundle("test", "text/javascript");

		bundle.setRoot("src/test/resources/tests");
		bundle.addPath("/test.coffee");
		bundle.addProcessor(processor);

		Assert.assertEquals("(function(){varsquare;square=function(x){returnx*x;};}).call(this);", bundle.process().replaceAll("\\s", ""));
	}

}
