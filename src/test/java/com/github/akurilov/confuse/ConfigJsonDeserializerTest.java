package com.github.akurilov.confuse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Test;

import java.util.Map;

public class ConfigJsonDeserializerTest {

	private static ObjectMapper objectMapper() {
		final Module jacksonModule = new SimpleModule()
			.addDeserializer(Config.class, new ConfigJsonDeserializer(Config.class));
		return new ObjectMapper()
			.registerModule(jacksonModule)
			.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
			.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
			.enable(DeserializationFeature.USE_LONG_FOR_INTS)
			.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
			.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
	}

	@Test
	public final void testMapValue()
	throws Exception {

		final String jsonData = "{\n" +
			"\t\"a\" : null,\n" +
			"\t\"b\" : {\n" +
			"\t\t\"aa\" : [\n" +
			"\t\t\tnull,\n" +
			"\t\t\tnull\n" +
			"\t\t],\n" +
			"\t\t\"bb\" : {\n" +
			"\t\t\t\"aaa\" : {\n" +
			"\t\t\t\t\"bar\" : 123,\n" +
			"\t\t\t\t\"foo\" : null\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}";
		final Config config = objectMapper().readValue(jsonData, Config.class);
		final Map<String, Object> v = config.mapVal("b-bb-aaa");

	}

}
