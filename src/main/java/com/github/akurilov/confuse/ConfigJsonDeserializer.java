package com.github.akurilov.confuse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ConfigJsonDeserializer
extends StdDeserializer<Config> {

	public ConfigJsonDeserializer(final Class<? extends Config> vc) {
		super(vc);
	}

	@Override
	public final Config deserialize(final JsonParser p, final DeserializationContext ctx)
	throws IOException, JsonProcessingException {
		final Map<String, Object> node = p.readValueAs(
			new TypeReference<HashMap<String, Object>>() {}
		);
		return new BasicExtensibleConfig("-", node);
	}
}
