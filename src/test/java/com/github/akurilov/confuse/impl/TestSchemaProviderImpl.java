package com.github.akurilov.confuse.impl;

import com.github.akurilov.confuse.SchemaProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSchemaProviderImpl
implements SchemaProvider {

	@Override
	public String id() {
		return "test";
	}

	@Override
	public Map<String, Object> schema() {
		return new HashMap<String, Object>() {{
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
							put("c", int.class);
						}}
					);
				}}
			);
			put("d", Double.TYPE);
			put(
				"e",
				new HashMap<String, Object>() {{
					put("e", boolean.class);
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
	}
}
