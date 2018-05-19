package com.github.akurilov.confuse;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

		final Optional<Config> optionalConfig = ConfigProvider.resolve(
			"test", getClass().getClassLoader(), "-", schema
		);
		if(optionalConfig.isPresent()) {
			final Config config = optionalConfig.get();
			assertTrue(
				Arrays.equals(
					new char[] { 'q', 'a', 'z', 'x', 's', 'w', 'e', 'd', 'c' },
					(char[]) config.val("qaz-xsw-edc")
				)
			);
		} else {
			fail("No config resolved");
		}
	}
}
