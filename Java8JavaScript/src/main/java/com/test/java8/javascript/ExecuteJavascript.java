package com.test.java8.javascript;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.IOUtils;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("restriction")
public class ExecuteJavascript {

	public static void main(String[] args) {

		try {

			int max = 1_000_000;

			for (int test = 0; test < 4; test++) {

				{
					long time1 = System.currentTimeMillis();
					List<Integer> items = null;

					for (int i = 0; i < max; i++) {

						items = Arrays.asList(4, 2, 6, 5, 3, 9);
						Collections.sort(items);
					}

					System.out.println(items.stream().map(item -> item.toString()).collect(Collectors.joining(", ")));

					long time2 = System.currentTimeMillis();
					System.out.println("Java Built-In Sorting: " + (time2 - time1) / 1000.0);
				}

				{
					long time5 = System.currentTimeMillis();
					List<Integer> items = null;

					for (int i = 0; i < max; i++) {

						items = new LinkedList<Integer>();
						items.addAll(Arrays.asList(4, 2, 6, 5, 3, 9));
						QSort.sort(items);
					}

					System.out.println(items.stream().map(item -> item.toString()).collect(Collectors.joining(", ")));

					long time6 = System.currentTimeMillis();
					System.out.println("Java Custom Sorting (LinkedList): " + (time6 - time5) / 1000.0);
				}

				{
					long time7 = System.currentTimeMillis();
					List<Integer> items = null;

					for (int i = 0; i < max; i++) {

						items = new ArrayList<Integer>();
						items.addAll(Arrays.asList(4, 2, 6, 5, 3, 9));
						QSort.sort(items);
					}

					System.out.println(items.stream().map(item -> item.toString()).collect(Collectors.joining(", ")));

					long time8 = System.currentTimeMillis();
					System.out.println("Java Custom Sorting (ArrayList): " + (time8 - time7) / 1000.0);
				}

				{
					InputStream inputStream = ExecuteJavascript.class.getClassLoader().getResourceAsStream("com/test/java8/javascript/javascript.js");
					String javascript = IOUtils.toString(inputStream, "UTF-8");
					ScriptEngineManager factory = new ScriptEngineManager();
					ScriptEngine engine = factory.getEngineByName("nashorn");

					final Compilable compilable = (Compilable) engine;
					final Invocable invocable = (Invocable) engine;
					final CompiledScript compiled = compilable.compile(javascript);
					compiled.eval();

					long time3 = System.currentTimeMillis();
					Object object = null; 
							
					for (int i = 0; i < max; i++) {

						object = invocable.invokeFunction("main");
						
					}
				
					
					long time4 = System.currentTimeMillis();

					System.out.println("Script Sorting: " + (time4 - time3) / 1000.0);
				}
				
				{
					InputStream inputStream = ExecuteJavascript.class.getClassLoader().getResourceAsStream("com/test/java8/javascript/javascript-built-in.js");
					String javascript = IOUtils.toString(inputStream, "UTF-8");
					ScriptEngineManager factory = new ScriptEngineManager();
					ScriptEngine engine = factory.getEngineByName("nashorn");

					final Compilable compilable = (Compilable) engine;
					final Invocable invocable = (Invocable) engine;
					final CompiledScript compiled = compilable.compile(javascript);
					compiled.eval();

					long time1 = System.currentTimeMillis();

					for (int i = 0; i < max; i++) {

						Object jsObject = invocable.invokeFunction("main");
					}

					long time2 = System.currentTimeMillis();

					System.out.println("Script Built-In Sorting: " + (time2 - time1) / 1000.0);
				}

				System.out.println(" ---------------------------------------------- ");
			}

			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
