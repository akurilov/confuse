package com.github.akurilov.confuse;

public final class InvalidValueTypeException
extends IllegalStateException {

	private final String path;

	public InvalidValueTypeException(
		final String path, final Class expectedType, final Class actualType
	) {
		super(
			"Invalid val type @ " + path + ", expected: \"" + expectedType + "\", actual: \""
				+ actualType + "\""
		);
		this.path = path;
	}

	public final String path() {
		return path;
	}
}
