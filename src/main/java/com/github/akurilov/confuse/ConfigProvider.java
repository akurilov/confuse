package com.github.akurilov.confuse;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public interface ConfigProvider {

	String id();

	Config config(final String pathSep, final Map<String, Object> schema)
	throws Exception;

	static Optional<Config> resolve(
		final String id, final ClassLoader clsLoader, final String pathSep,
		final Map<String, Object> schema
	) throws Exception {
		final ServiceLoader<ConfigProvider> loader = ServiceLoader.load(
			ConfigProvider.class, clsLoader
		);
		for(final ConfigProvider configProvider: loader) {
			if(id.equals(configProvider.id())) {
				return Optional.of(configProvider.config(pathSep, schema));
			}
		}
		return Optional.empty();
	}
}
