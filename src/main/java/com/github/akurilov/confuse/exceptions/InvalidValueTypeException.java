package com.github.akurilov.confuse.exceptions;

public final class InvalidValueTypeException
extends IllegalStateException {

	private final String path;
	private final Class expectedType;
	private final Class actualType;

	public InvalidValueTypeException(
		final String path, final Class expectedType, final Class actualType
	) {
		super(
			"Invalid value type @ " + path + ", expected: \"" + expectedType + "\", actual: \""
				+ actualType + "\""
		);
		this.path = path;
		this.expectedType = expectedType;
		this.actualType = actualType;
	}

	public final String path() {
		return path;
	}

	public final Class expectedType() {
		return expectedType;
	}

	public final Class actualType() {
		return actualType;
	}
}
