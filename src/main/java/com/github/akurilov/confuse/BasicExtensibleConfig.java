package com.github.akurilov.confuse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class BasicExtensibleConfig
implements ExtensibleConfig {

	private final String pathSep;
	private final Map<String, Object> node;

	public BasicExtensibleConfig(final String pathSep) {
		this(pathSep, new HashMap<>());
	}

	public BasicExtensibleConfig(final String pathSep, final Map<String, Object> node) {
		this.pathSep = pathSep;
		this.node = node;
	}

	/**
	 Cloning constructor
	 @param other the config instance to clone
	 */
	public BasicExtensibleConfig(final ExtensibleConfig other) {
		this.pathSep = other.pathSep();
		final Map<String, Object> otherNode = other.mapVal("");
		this.node = new HashMap<>(otherNode.size());
		for(final Map.Entry<String, Object> entry : otherNode.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			if(value instanceof BasicExtensibleConfig) {
				node.put(key, new BasicExtensibleConfig((BasicExtensibleConfig) value));
			} else {
				node.put(key, value);
			}
		}
	}

	@Override
	public final String pathSep() {
		return pathSep;
	}

	@Override
	public final Object val(final String path)
	throws InvalidValuePathException, NoSuchElementException {
		if(path == null || path.isEmpty()) {
			return node;
		}
		final int sepPos = path.indexOf(pathSep);
		if(sepPos == 0 || sepPos == path.length() - 1) {
			throw new InvalidValuePathException(path);
		}
		if(sepPos > 0) {
			final String key = path.substring(0, sepPos);
			final String subPath = path.substring(sepPos + 1);
			final Object child = node.get(key);
			if(child instanceof ExtensibleConfig) {
				try {
					return ((ExtensibleConfig) child).val(subPath);
				} catch(final InvalidValuePathException e) {
					throw new InvalidValuePathException(key + pathSep + e.getMessage());
				} catch(final NoSuchElementException e) {
					throw new NoSuchElementException(key + pathSep + e.getMessage());
				}
			} else {
				throw new NoSuchElementException(path);
			}
		} else {
			return node.get(path);
		}
	}

	@Override
	public final void val(final String path, final Object val)
	throws InvalidValuePathException {
		final int sepPos = path.indexOf(pathSep);
		if(sepPos == 0 || sepPos == path.length() - 1) {
			throw new InvalidValuePathException(path);
		}
		if(sepPos > 0) {
			final String key = path.substring(0, sepPos);
			final String subPath = path.substring(sepPos + 1);
			final Object child = node.get(key);
			final ExtensibleConfig childConfig;
			if(child instanceof ExtensibleConfig) {
				childConfig = (ExtensibleConfig) child;
			} else {
				childConfig = new BasicExtensibleConfig(pathSep);
				node.put(key, childConfig);
			}
			childConfig.val(subPath, val);
		} else {
			node.put(path, val);
		}
	}

	@Override
	public final String stringVal(final String path)
	throws InvalidValuePathException, NoSuchElementException {
		return val(path).toString();
	}

	@Override
	public final boolean boolVal(final String path)
	throws InvalidValuePathException, NoSuchElementException {
		final Object v = val(path);
		if(v instanceof String) {
			return Boolean.parseBoolean((String) v);
		} else {
			try {
				return (boolean) v;
			} catch(final ClassCastException e) {
				throw new InvalidValueTypeException(path, Boolean.TYPE, v.getClass());
			}
		}
	}

	@Override
	public final int intVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException {
		final Object v = val(path);
		if(v instanceof String) {
			return Integer.parseInt((String) v);
		} else {
			try {
				return (int) v;
			} catch(final ClassCastException e) {
				throw new InvalidValueTypeException(path, Integer.TYPE, v.getClass());
			}
		}
	}

	@Override
	public final long longVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException {
		final Object v = val(path);
		if(v instanceof String) {
			return Long.parseLong((String) v);
		} else {
			try {
				return (long) v;
			} catch(final ClassCastException e) {
				throw new InvalidValueTypeException(path, Long.TYPE, v.getClass());
			}
		}
	}

	@Override
	public final double doubleVal(final String path)
	throws InvalidValuePathException, NoSuchElementException, NumberFormatException {
		final Object v = val(path);
		if(v instanceof String) {
			return Double.parseDouble((String) v);
		} else {
			try {
				return (double) v;
			} catch(final ClassCastException e) {
				throw new InvalidValueTypeException(path, Double.TYPE, v.getClass());
			}
		}
	}

	@Override @SuppressWarnings("unchecked")
	public final <V> Map<String, V> mapVal(final String path)
	throws InvalidValuePathException, NoSuchElementException {
		final Object v = val(path);
		try {
			return (Map<String, V>) v;
		} catch(final ClassCastException e) {
			throw new InvalidValueTypeException(path, Map.class, v.getClass());
		}
	}

	@Override @SuppressWarnings("unchecked")
	public final <E> List<E> listVal(final String path)
	throws InvalidValuePathException, NoSuchElementException {
		final Object v = val(path);
		try {
			return (List<E>) v;
		} catch(final ClassCastException e) {
			throw new InvalidValueTypeException(path, List.class, v.getClass());
		}
	}

	@Override
	public final boolean equals(final Object o) {
		if(o == null) {
			return false;
		}
		if(!(o instanceof BasicExtensibleConfig)) {
			return false;
		}
		final BasicExtensibleConfig other = (BasicExtensibleConfig) o;
		for(final String key : node.keySet()) {
			final Object val = node.get(key);
			if(val == null) {
				if(other.node.get(key) != null) {
					return false;
				}
			} else if(!val.equals(other.node.get(key))) {
				return false;
			}
		}
		return true;
	}
}
