package com.github.akurilov.confuse;

import java.util.Map;
import java.util.ServiceLoader;

public interface SchemaProvider {

	String id();

	Map<String, Object> schema();

	static Map<String, Object> resolve(final String id, final ClassLoader clsLoader) {
		final ServiceLoader<SchemaProvider> loader = ServiceLoader.load(
			SchemaProvider.class, clsLoader
		);
		for(final SchemaProvider schemaProvider : loader) {
			if(id.equals(schemaProvider.id())) {
				return schemaProvider.schema();
			}
		}
		return null;
	}
}
