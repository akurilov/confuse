package com.github.akurilov.confuse;

import com.github.akurilov.confuse.exceptions.InvalidValuePathException;
import com.github.akurilov.confuse.exceptions.InvalidValueTypeException;
import com.github.akurilov.confuse.impl.BasicConfig;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ConfigTest {

	private static Map<String, Object> SCHEMA = new HashMap<String, Object>() {{
		put("a", Object.class);
		put(
			"b",
			new HashMap<String, Object>() {{
				put("b", String.class);
			}}
		);
		put(
			"c",
			new HashMap<String, Object>() {{
				put(
					"c",
					new HashMap<String, Object>() {{
						put("c", Integer.TYPE);
					}}
				);
			}}
		);
		put("d", Double.TYPE);
		put(
			"e",
			new HashMap<String, Object>() {{
				put("e", Boolean.TYPE);
			}}
		);
		put(
			"f",
			new HashMap<String, Object>() {{
				put(
					"f",
					new HashMap<String, Object>() {{
						put("f", List.class);
					}}
				);
			}}
		);
		put("g", Map.class);
	}};

	@Test
	public final void testClone()
	throws Exception {

		final Config src = new BasicConfig("-", SCHEMA);
		src.val("a", null);
		src.val("b-b", "stringvalue");
		src.val("c-c-c", 42);
		src.val("d", 3.1415926);
		src.val("e-e", false);
		src.val("f-f-f", Arrays.asList("foo", "bar", 123, null));
		src.val(
			"g",
			new HashMap<String, String>() {{
				put("foo", "bar");
				put("hello", "world");
			}}
		);

		final Config dst = new BasicConfig(src);

		src.val("a", "a");
		src.val("c-c-c", 0xCCC);

		assertNull(dst.val("a"));
		assertEquals(src.stringVal("b-b"), dst.stringVal("b-b"));
		assertNotEquals(src.intVal("c-c-c"), dst.intVal("c-c-c"));
		assertEquals(src.doubleVal("d"), dst.doubleVal("d"), 0);
		assertEquals(src.boolVal("e-e"), dst.boolVal("e-e"));
		assertEquals(src.listVal("f-f-f"), dst.listVal("f-f-f"));
		assertEquals(src.mapVal("g"), dst.mapVal("g"));
	}

	@Test
	public final void testEquals()
	throws Exception {

		final Config src = new BasicConfig("-", SCHEMA);
		src.val("a", null);
		src.val("b-b", "stringvalue");
		src.val("c-c-c", 42);
		src.val("d", 3.1415926);
		src.val("e-e", false);
		src.val("f-f-f", Arrays.asList("foo", "bar", 123, null));
		src.val("g-foo", "bar");
		src.val("g-hello", "world");

		final Config dst = new BasicConfig(src);

		assertEquals(src, dst);
	}

	@Test
	public final void testNotEquals()
	throws Exception {

		final Config src = new BasicConfig("-", SCHEMA);
		src.val("a", null);
		src.val("b-b", "stringvalue");
		src.val("c-c-c", 42);
		src.val("d", 3.1415926);
		src.val("e-e", false);
		src.val("f-f-f", Arrays.asList("foo", "bar", 123, null));
		src.val(
			"g",
			new HashMap<String, String>() {{
				put("foo", "bar");
				put("hello", "world");
			}}
		);

		final Config dst = new BasicConfig(src);
		dst.val("e-e", true);

		assertNotEquals(src, dst);
	}

	@Test
	public final void testInvalidValuePath()
	throws Exception {
		final Config src = new BasicConfig(
			"-",
			new HashMap<String, Object>() {{
				put("a", String.class);
			}}
		);
		src.val("a", "b");
		final String invalidValPath = "a-";
		try {
			src.val(invalidValPath);
			fail();
		} catch(final InvalidValuePathException e) {
			assertEquals(invalidValPath, e.path());
		}
	}

	@Test
	public final void testNoSuchElement()
	throws Exception {

		final Config src = new BasicConfig(
			"-",
			new HashMap<String, Object>() {{
				put("foo", Boolean.TYPE);
				put("bar", Object.class);
			}}
		);
		src.val("foo", true);
		src.val("bar", null);

		assertNull(src.val("bar"));
		final String missingValPath = "bar-foo";
		try {
			src.val("bar-foo");
			fail();
		} catch(final NoSuchElementException e) {
			assertEquals(missingValPath, e.getMessage());
		}
	}

	@Test
	public final void testInvalidValueType()
	throws Exception {

		final Config src = new BasicConfig(
			"-",
			new HashMap<String, Object>() {{
				put(
					"a",
					new HashMap<String, Object>() {{
						put("aa", String.class);
						put("bb", Integer.TYPE);
					}}
				);
			}}
		);
		final String path1 = "a-aa";
		final Object actualValue1 = "c";
		final String path2 = "a-bb";
		final int actualValue2 = 42;
		src.val(path1, actualValue1);
		src.val(path2, actualValue2);

		try {
			final long v = src.longVal(path1);
			fail();
		} catch(final NumberFormatException expected) {
		}

		try {
			final List v = src.listVal(path1);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals(path1, e.path());
			assertEquals(List.class, e.expectedType());
			assertEquals(String.class, e.actualType());
		}

		try {
			final boolean v = src.boolVal(path2);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals(path2, e.path());
			assertEquals(Boolean.TYPE, e.expectedType());
			assertEquals(Integer.class, e.actualType());
		}

		final long v = src.longVal(path2);
		assertEquals(actualValue2, v);
	}

	@Test
	public final void testChildConfig()
	throws Exception {

		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put(
				"qwe",
				new HashMap<String, Object>() {{
					put(
						"rty",
						new HashMap<String, Object>() {{
							put("uio", int.class);
							put("pas", List.class);
						}}
					);
				}}
			);
			put(
				"dfg",
				new HashMap<String, Object>() {{
					put("hjk", boolean.class);
				}}
			);
		}};

		final Config config = new BasicConfig("-", schema);

		config.val("qwe-rty-uio", 0);
		config.val("qwe-rty-pas", Arrays.asList(1, 2, 3));
		config.val("dfg-hjk", true);

		final Config qweConfig = (Config) config.val("qwe");
		final Config qweRtyConfig = (Config) config.val("qwe-rty");
		final Config rtyConfig = (Config) qweConfig.val("rty");
		assertEquals(qweRtyConfig, rtyConfig);
		assertEquals(0, qweRtyConfig.intVal("uio"));
		assertEquals(Arrays.asList(1, 2, 3), rtyConfig.listVal("pas"));
		final Config dfgConfig = (Config) config.val("dfg");
		assertTrue(dfgConfig.boolVal("hjk"));
	}

	@Test
	public final void testSchemaMismatchPathTest()
	throws Exception {

		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put(
				"qwe",
				new HashMap<String, Object>() {{
					put(
						"rty",
						new HashMap<String, Object>() {{
							put("uio", int.class);
							put("pas", List.class);
						}}
					);
				}}
			);
			put(
				"dfg",
				new HashMap<String, Object>() {{
					put("hjk", boolean.class);
				}}
			);
		}};

		final Config config = new BasicConfig("-", schema);

		try {
			config.val("qwe-rty-abc", 42L);
			fail();
		} catch(final InvalidValuePathException e) {
			assertEquals("qwe-rty-abc", e.path());
		}

		try {
			config.val("lzx", new HashMap<String, Object>());
			fail();
		} catch(final InvalidValuePathException e) {
			assertEquals("lzx", e.path());
		}
	}

	@Test
	public final void testSchemaMismatchTypeTest()
	throws Exception {

		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put("abc", int.class);
			put("def", long.class);
			put("ghi", float.class);
			put("jkl", byte.class);
			put("mno", String.class);
		}};

		final Config config = new BasicConfig("-", schema);

		try {
			config.val("abc", Long.MAX_VALUE);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("abc", e.path());
			assertEquals(int.class, e.expectedType());
			assertEquals(Long.class, e.actualType());
		}

		try {
			config.val("def", null);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("def", e.path());
			assertEquals(long.class, e.expectedType());
			assertNull(e.actualType());
		}

		try {
			config.val("ghi", Math.PI);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("ghi", e.path());
			assertEquals(float.class, e.expectedType());
			assertEquals(Double.class, e.actualType());
		}

		try {
			config.val("jkl", 0x10);
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("jkl", e.path());
			assertEquals(byte.class, e.expectedType());
			assertEquals(Integer.class, e.actualType());
		}

		try {
			config.val("mno", new ArrayList<>());
			fail();
		} catch(final InvalidValueTypeException e) {
			assertEquals("mno", e.path());
			assertEquals(String.class, e.expectedType());
			assertEquals(ArrayList.class, e.actualType());
		}
	}

	@Test
	public final void testTypeCast()
	throws Exception {
		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put("a", double.class);
		}};
		final Config config = new BasicConfig("-", schema);
		config.val("a", 42);
	}

	@Test
	public final void testDeepToMap()
	throws Exception {

		final Config src = new BasicConfig("-", SCHEMA);
		src.val("a", null);
		src.val("b-b", "stringvalue");
		src.val("c-c-c", 42);
		src.val("d", 3.1415926);
		src.val("e-e", false);
		src.val("f-f-f", Arrays.asList("foo", "bar", 123, null));
		src.val(
			"g",
			new HashMap<String, String>() {{
				put("foo", "bar");
				put("hello", "world");
			}}
		);

		final Map<String, Object> dst = Config.deepToMap(src);
		assertEquals(src.val("a"), dst.get("a"));
		assertEquals(src.stringVal("b-b"), ((Map) dst.get("b")).get("b"));
		assertEquals(src.intVal("c-c-c"), ((Map) ((Map) dst.get("c")).get("c")).get("c"));
		assertEquals(src.doubleVal("d"), (Double) dst.get("d"), 0);
		assertEquals(src.boolVal("e-e"), ((Map) dst.get("e")).get("e"));
		assertEquals(src.listVal("f-f-f"), ((Map) ((Map) dst.get("f")).get("f")).get("f"));
		assertEquals(src.mapVal("g"), dst.get("g"));
	}
}
