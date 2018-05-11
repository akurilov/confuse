package com.github.akurilov.confuse;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public final class ExtensibleConfigJsonSerializerTest {

	private static ObjectWriter getObjectWriter() {
		final Module jacksonModule = new SimpleModule()
			.addSerializer(
				ExtensibleConfig.class,
				new ExtensibleConfigJsonSerializer(ExtensibleConfig.class)
			);
		final ObjectMapper jacksonMapper = new ObjectMapper()
			.registerModule(jacksonModule)
			.enable(SerializationFeature.INDENT_OUTPUT);
		final DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(
			"\t", DefaultIndenter.SYS_LF
		);
		final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);
		return jacksonMapper.writer(printer);
	}

	@Test
	public final void arrayValueTest()
	throws Exception {
		final ExtensibleConfig config = new BasicExtensibleConfig("-");
		config.val("a", Arrays.asList(1L, "2", 3, 4.0));
		final String actualJsonData = getObjectWriter().writeValueAsString(config);
		final String expectedJsonData = "{\n" +
			"\t\"a\" : [\n" +
			"\t\t1,\n" +
			"\t\t\"2\",\n" +
			"\t\t3,\n" +
			"\t\t4.0\n" +
			"\t]\n" +
			"}";
		assertEquals(expectedJsonData, actualJsonData);
	}

	@Test
	public final void mapValueTest()
	throws Exception {
		final ExtensibleConfig config = new BasicExtensibleConfig("-");
		config.val(
			"a",
			new HashMap<String, Object>() {{
				put("bb", 123);
				put("cc", "456");
				put(
					"dd",
					new HashMap<String, Object>() {{
						put("aaa", null);
						put("bbb", 3.1415926);
					}}
				);
			}}
		);
		final String actualJsonData = getObjectWriter().writeValueAsString(config);
		final String expectedJsonData = "{\n" +
			"\t\"a\" : {\n" +
			"\t\t\"bb\" : 123,\n" +
			"\t\t\"cc\" : \"456\",\n" +
			"\t\t\"dd\" : {\n" +
			"\t\t\t\"aaa\" : null,\n" +
			"\t\t\t\"bbb\" : 3.1415926\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}";
		assertEquals(expectedJsonData, actualJsonData);
	}

	@Test
	public final void nullValuesTest()
	throws Exception {

		final ExtensibleConfig config = new BasicExtensibleConfig("-");
		config.val("a", null);
		config.val("b-aa", Arrays.asList(null, null));
		config.val(
			"b-bb-aaa",
			new HashMap<String, Object>() {{
				put("foo", null);
				put("bar", null);
			}}
		);

		final String actualJsonData = getObjectWriter().writeValueAsString(config);
		final String expectedJsonData = "{\n" +
			"\t\"a\" : null,\n" +
			"\t\"b\" : {\n" +
			"\t\t\"aa\" : [\n" +
			"\t\t\tnull,\n" +
			"\t\t\tnull\n" +
			"\t\t],\n" +
			"\t\t\"bb\" : {\n" +
			"\t\t\t\"aaa\" : {\n" +
			"\t\t\t\t\"bar\" : null,\n" +
			"\t\t\t\t\"foo\" : null\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}";
		assertEquals(expectedJsonData, actualJsonData);
	}

	@Test
	public final void numValuesTest()
	throws Exception {
		final ExtensibleConfig config = new BasicExtensibleConfig("-");
		config.val("a", -1.234);
		config.val("b", 5);
		config.val("c", Long.MAX_VALUE);
		config.val("d", Double.MAX_VALUE);
		config.val("e", Double.MIN_VALUE);
		config.val("f", Double.NEGATIVE_INFINITY);
		config.val("g", Double.POSITIVE_INFINITY);
		config.val("i", new BigDecimal("+1234567890E+1234567890"));

		final String actualJsonData = getObjectWriter().writeValueAsString(config);
		final String expectedJsonData = "{\n" +
			"\t\"a\" : -1.234,\n" +
			"\t\"b\" : 5,\n" +
			"\t\"c\" : 9223372036854775807,\n" +
			"\t\"d\" : 1.7976931348623157E308,\n" +
			"\t\"e\" : 4.9E-324,\n" +
			"\t\"f\" : \"-Infinity\",\n" +
			"\t\"g\" : \"Infinity\",\n" +
			"\t\"i\" : 1.234567890E+1234567899\n" +
			"}";
		assertEquals(expectedJsonData, actualJsonData);
	}
}
