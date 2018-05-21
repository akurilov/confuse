package com.github.akurilov.confuse;

import com.github.akurilov.confuse.impl.BasicConfig;

import static com.github.akurilov.commons.collection.TreeUtil.reduceForest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface ConfigProvider {

	String id();

	Config config(final String pathSep, final Map<String, Object> schema)
	throws Exception;

	static List<Config> resolve(
		final String id, final ClassLoader clsLoader, final String pathSep,
		final Map<String, Object> schema
	) throws Exception {
		final List<Config> resolvedConfigs = new ArrayList<>();
		final ServiceLoader<ConfigProvider> configProviderLoader = ServiceLoader.load(
			ConfigProvider.class, clsLoader
		);
		for(final ConfigProvider configProvider: configProviderLoader) {
			if(id.equals(configProvider.id())) {
				resolvedConfigs.add(configProvider.config(pathSep, schema));
			}
		}
		return resolvedConfigs;
	}

	static Config resolveAndReduce(
		final String id, final ClassLoader clsLoader, final String pathSep,
		final Map<String, Object> schema
	) throws Exception {
		final List<Config> resolvedConfigs = resolve(id, clsLoader, pathSep, schema);
		if(resolvedConfigs == null || resolvedConfigs.size() == 0) {
			return null;
		}
		final List<Map<String, Object>> configForest = resolvedConfigs
			.stream()
			.map(c -> c.mapVal(Config.ROOT_PATH))
			.collect(Collectors.toList());
		final Map<String, Object> configTree = reduceForest(configForest);
		return new BasicConfig(pathSep, schema, configTree);
	}
}
