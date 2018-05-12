package com.github.akurilov.confuse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public final class ConfigJsonSerializer
extends StdSerializer<Config> {

	public ConfigJsonSerializer(final Class<Config> t) {
		super(t);
	}

	@Override
	public final void serialize(
		final Config value, final JsonGenerator gen, final SerializerProvider provider
	) throws IOException {
		gen.writeStartObject();
		final Map<String, Object> valueAsMap = value.mapVal("");
		valueAsMap
			.forEach(
				(k, v) -> {
					try {
						if(v == null) {
							gen.writeNullField(k);
						} else if(v instanceof Boolean) {
							gen.writeBooleanField(k, (boolean) v);
						} else if(v instanceof Short) {
							gen.writeNumberField(k, (short) v);
						} else if(v instanceof Integer) {
							gen.writeNumberField(k, (int) v);
						} else if(v instanceof Long) {
							gen.writeNumberField(k, (long) v);
						} else if(v instanceof Float) {
							gen.writeNumberField(k, (float) v);
						} else if(v instanceof Double) {
							gen.writeNumberField(k, (double) v);
						} else if(v instanceof BigDecimal) {
							gen.writeNumberField(k, (BigDecimal) v);
						} else if(v instanceof String) {
							gen.writeStringField(k, (String) v);
						} else if(v instanceof List) {
							gen.writeArrayFieldStart(k);
							writeArrayElements(gen, provider, (List) v);
							gen.writeEndArray();
						} else {
							gen.writeObjectField(k, v);
						}
					} catch(final IOException e) {
						e.printStackTrace(System.err);
					}
				}
			);
		gen.writeEndObject();
	}

	private void writeArrayElements(
		final JsonGenerator gen, final SerializerProvider provider, final List<Object> values
	) {
		values.forEach(
			v -> {
				try {
					if(v == null) {
						gen.writeNull();
					} else if(v instanceof Boolean) {
						gen.writeBoolean((boolean) v);
					} else if(v instanceof Short) {
						gen.writeNumber((short) v);
					} else if(v instanceof Integer) {
						gen.writeNumber((int) v);
					} else if(v instanceof Long) {
						gen.writeNumber((long) v);
					} else if(v instanceof Float) {
						gen.writeNumber((float) v);
					} else if(v instanceof Double) {
						gen.writeNumber((double) v);
					} else if(v instanceof BigDecimal) {
						gen.writeNumber((BigDecimal) v);
					} else if(v instanceof String) {
						gen.writeString((String) v);
					} else if(v instanceof List) {
						gen.writeStartArray();
						writeArrayElements(gen, provider, (List) v);
						gen.writeEndArray();
					} else {
						gen.writeObject(v);
					}
				} catch(final IOException e) {
					e.printStackTrace(System.err);
				}
			}
		);
	}
}
