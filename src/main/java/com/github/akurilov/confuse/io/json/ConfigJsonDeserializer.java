package com.github.akurilov.confuse.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.github.akurilov.confuse.ExtensibleConfig;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class ExtensibleConfigJsonDeserializer<C extends ExtensibleConfig>
extends StdDeserializer<C> {

	private final String pathSep;
	private final Constructor<C> constructor;

	/**
	 @param pathSep path separator for the new config instance
	 @param implCls the particular config implementation class ref
	 @throws NoSuchMethodException if no constructor is declared with (String, Map) args
	 */
	public ExtensibleConfigJsonDeserializer(final String pathSep, final Class<C> implCls)
	throws NoSuchMethodException {
		super(implCls);
		this.pathSep = pathSep;
		this.constructor = implCls.getConstructor(String.class, Map.class);
	}

	@Override
	public final C deserialize(final JsonParser p, final DeserializationContext ctx)
	throws IOException, JsonProcessingException {
		final Map<String, Object> node = p.readValueAs(
			new TypeReference<HashMap<String, Object>>() {}
		);
		try {
			return constructor.newInstance(pathSep, node);
		} catch(final ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
