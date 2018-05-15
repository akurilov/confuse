package com.github.akurilov.confuse;

import com.github.akurilov.confuse.exceptions.InvalidValueTypeException;
import com.github.akurilov.confuse.impl.BasicConfig;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolveSchemaTest {

	@Test
	public final void testValidValues()
	throws Exception {

		final Map<String, Object> schema = SchemaProvider.resolveAndReduce(
			"test", Thread.currentThread().getContextClassLoader()
		);
		assertNotNull(schema);
		final Config config = new BasicConfig("-", schema);

		config.val("a", null);
		config.val("b-b", "stringvalue");
		config.val("c-c-c", 42);
		config.val("d", 3.1415926);
		config.val("e-e", false);
		config.val("f-f-f", Arrays.asList("foo", "bar", 123, null));
		config.val(
			"g",
			new HashMap<String, String>() {{
				put("foo", "bar");
				put("hello", "world");
			}}
		);
	}

	@Test
	public final void testInvalidValues()
	throws Exception {

		final Map<String, Object> schema = SchemaProvider.resolveAndReduce(
			"test", Thread.currentThread().getContextClassLoader()
		);
		assertNotNull(schema);
		final Config config = new BasicConfig("-", schema);

		try {
			config.val("b-b", 123);
		} catch(final InvalidValueTypeException e) {
			assertEquals("b-b", e.path());
			assertEquals(Integer.class, e.actualType());
			assertEquals(String.class, e.expectedType());
		}

		try {
			config.val("c-c-c", 3.1415926);
		} catch(final InvalidValueTypeException e) {
			assertEquals("c-c-c", e.path());
			assertEquals(Double.class, e.actualType());
			assertEquals(int.class, e.expectedType());
		}

		try {
			config.val("d", null);
		} catch(final InvalidValueTypeException e) {
			assertEquals("d", e.path());
			assertNull(e.actualType());
			assertEquals(double.class, e.expectedType());
		}

		try {
			config.val("e-e", "yohoho");
		} catch(final InvalidValueTypeException e) {
			assertEquals("e-e", e.path());
			assertEquals(String.class, e.actualType());
			assertEquals(boolean.class, e.expectedType());
		}

		final Map<String, String> fffVal = new HashMap<String, String>() {{
			put("foo", "bar");
			put("hello", "world");
		}};
		try {
			config.val("f-f-f", fffVal);
		} catch(final InvalidValueTypeException e) {
			assertEquals("f-f-f", e.path());
			assertEquals(fffVal.getClass(), e.actualType());
			assertEquals(List.class, e.expectedType());
		}

		final List gVal = Arrays.asList("foo", "bar", 123, null);
		try {
			config.val("g", gVal);
		} catch(final InvalidValueTypeException e) {
			assertEquals("g", e.path());
			assertEquals(gVal.getClass(), e.actualType());
			assertEquals(Map.class, e.expectedType());
		}
	}
}
