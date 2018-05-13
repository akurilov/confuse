package com.github.akurilov.confuse.exceptions;

public class InvalidValuePathException
extends IllegalArgumentException {

	private final String path;

	public InvalidValuePathException(final String path) {
		super("Invalid value path: " + path);
		this.path = path;
	}

	public final String path() {
		return path;
	}
}
