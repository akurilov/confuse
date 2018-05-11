package com.github.akurilov.confuse;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class BasicExtensibleConfigTest {

	@Test
	public final void testClone()
	throws Exception {

		final ExtensibleConfig src = new BasicExtensibleConfig("-");
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

		final ExtensibleConfig dst = new BasicExtensibleConfig(src);

		assertEquals(src.val("a"), dst.val("a"));
		assertEquals(src.stringVal("b-b"), dst.stringVal("b-b"));
		assertEquals(src.intVal("c-c-c"), dst.intVal("c-c-c"));
		assertEquals(src.doubleVal("d"), dst.doubleVal("d"), 0);
		assertEquals(src.boolVal("e-e"), dst.boolVal("e-e"));
		assertEquals(src.listVal("f-f-f"), dst.listVal("f-f-f"));
		assertEquals(src.mapVal("g"), dst.mapVal("g"));
	}

	@Test
	public final void testEquals()
	throws Exception {

		final ExtensibleConfig src = new BasicExtensibleConfig("-");
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

		final ExtensibleConfig dst = new BasicExtensibleConfig(src);

		assertEquals(src, dst);
	}

	@Test
	public final void testNotEquals()
	throws Exception {

		final ExtensibleConfig src = new BasicExtensibleConfig("-");
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

		final ExtensibleConfig dst = new BasicExtensibleConfig(src);
		dst.val("e-e", true);

		assertNotEquals(src, dst);
	}

	@Test
	public final void testInvalidValuePath()
	throws Exception {

		final ExtensibleConfig src = new BasicExtensibleConfig("-");
		src.val("a", "b");
		final String invalidValPath = "a-";
		try {
			src.val(invalidValPath);
			fail();
		} catch(final InvalidValuePathException e) {
			assertEquals(invalidValPath, e.getMessage());
		}
	}

	@Test
	public final void testNoSuchElement()
	throws Exception {

		final ExtensibleConfig src = new BasicExtensibleConfig("-");
		src.val("foo", true);
		src.val("bar", null);

		assertNull(src.val("bar"));
		final String missingValPath = "bar-foo";
		try {
			src.val("bar-foo");
		} catch(final NoSuchElementException e) {
			assertEquals(missingValPath, e.getMessage());
		}
	}
}
