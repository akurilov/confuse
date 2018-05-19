package com.github.akurilov.confuse.impl;

import com.github.akurilov.confuse.Config;
import com.github.akurilov.confuse.ConfigProvider;

import java.util.Map;

public class TestConfigProviderImpl
implements ConfigProvider {

	@Override
	public String id() {
		return "test";
	}

	@Override
	public Config config(final String pathSep, final Map<String, Object> schema) {
		final Config config = new BasicConfig(pathSep, schema);
		config.val(
			"qaz" + pathSep + "xsw" + pathSep + "edc",
			new char[] { 'q', 'a', 'z', 'x', 's', 'w', 'e', 'd', 'c' }
		);
		return config;
	}
}
