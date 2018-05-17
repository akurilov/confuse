package com.github.akurilov.confuse;

import com.github.akurilov.confuse.exceptions.InvalidValuePathException;
import com.github.akurilov.confuse.exceptions.InvalidValueTypeException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface Config
extends Serializable {

	String ROOT_PATH = "";

	/** Returns the path separator **/
	String pathSep();

	/** Returns the validation schema **/
	Map<String, Object> schema();

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
	throws InvalidValuePathException, InvalidValueTypeException;

	String stringVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	boolean boolVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	char charVal(final String path)
	throws InvalidValueTypeException, NoSuchElementException;

	byte byteVal(final String path)
	throws InvalidValueTypeException, NoSuchElementException;

	short shortVal(final String path)
	throws InvalidValueTypeException, NoSuchElementException, NumberFormatException;

	int intVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	long longVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	float floatVal(final String path)
	throws InvalidValueTypeException, NoSuchElementException, NumberFormatException;

	double doubleVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException;

	<V> Map<String, V> mapVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	<E> List<E> listVal(final String path)
	throws InvalidValuePathException, NoSuchElementException;

	Config configVal(final String path)
	throws InvalidValuePathException, InvalidValueTypeException;
}
