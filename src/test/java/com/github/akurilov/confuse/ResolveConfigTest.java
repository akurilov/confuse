package com.github.akurilov.confuse;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ResolveConfigTest {

	@Test
	public void test()
	throws Exception {

		final Map<String, Object> schema = new HashMap<String, Object>() {{
			put(
				"qaz",
				new HashMap<String, Object>() {{
					put(
						"xsw",
						new HashMap<String, Object>() {{
							put("edc", char[].class);
						}}
					);
				}}
			);
		}};

		final Config config = ConfigProvider.resolveAndReduce(
			"test", getClass().getClassLoader(), "-", schema
		);
		assertNotNull(config);
		assertTrue(
			Arrays.equals(
				new char[] { 'q', 'a', 'z', 'x', 's', 'w', 'e', 'd', 'c' },
				(char[]) config.val("qaz-xsw-edc")
			)
		);
	}
}
