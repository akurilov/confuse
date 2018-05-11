package com.github.akurilov.confuse;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public final class ExtensibleConfigJsonDeserializer
extends StdDeserializer<ExtensibleConfig> {

	public ExtensibleConfigJsonDeserializer(final Class<? extends ExtensibleConfig> vc) {
		super(vc);
	}

	@Override
	public final ExtensibleConfig deserialize(final JsonParser p, final DeserializationContext ctx)
	throws IOException, JsonProcessingException {
		p.getO
		return null;
	}
}
