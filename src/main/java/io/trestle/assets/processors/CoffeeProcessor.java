package io.trestle.assets.processors;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoffeeProcessor implements BundleProcessor {

	private static final Logger log = LoggerFactory.getLogger(CoffeeProcessor.class);

	private Object coffee;
	private Invocable invoker;

	public CoffeeProcessor() {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		invoker = (Invocable) engine;

		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("coffee-script.js");
		try {
			engine.eval(new InputStreamReader(stream));
			log.info("CoffeeScript Compiler v{} loaded", engine.eval("CoffeeScript.VERSION"));
			coffee = engine.eval("CoffeeScript");
		} catch (ScriptException e) {
			log.error("Unable to create Coffee compiler", e);
		}
	}

	@Override
	public String process(String content) {
		if (coffee == null) {
			return "console.log('coffee processor failed to initialize compiler');";
		}

		log.trace("CoffeeScript.compile('" + content + "');");

		try {
			return invoker.invokeMethod(coffee, "compile", content).toString();
		} catch (ScriptException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return "console.log('Error compiling coffee');";
	}

}
