package com.github.akurilov.confuse;

public class InvalidValuePathException
extends IllegalArgumentException {

	public InvalidValuePathException(final String path) {
		super(path);
	}
}
