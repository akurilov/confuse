package com.github.akurilov.confuse;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface ExtensibleConfig
extends Serializable {

	/** Returns the path separator **/
	String pathSep();

	/**
	 * Get the val
	 * @param path the path to the val, may be null either empty
	 * @return the val specified by the given path, returns the map of all values if path is null or empty
	 * @throws InvalidValuePathException if the path is not valid
	 * @throws NoSuchElementException if no val is associated with the given path
	 */
	Object val(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	/**
	 * Set the val
	 * @param path the path @ which the val should be set
	 * @param val the val to set
	 * @throws InvalidValuePathException if the path is not valid
	 */
	void val(final String path, final Object val)
	throws InvalidValuePathException;

	String stringVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	boolean boolVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	int intVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	long longVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	double doubleVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	<V> Map<String, V> mapVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	<E> List<E> listVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;
}
