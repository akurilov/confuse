package com.github.akurilov.confuse;

import static com.github.akurilov.commons.collection.TreeUtil.reduceForest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public interface SchemaProvider {

	String id();

	/**
	 @return the schema associated with the given schema provider
	 @throws Exception
	 */
	Map<String, Object> schema()
	throws Exception;

	/**
	 @param id schema id
	 @param clsLoader the class loader which will be used for the resolution
	 @return the list of schemes identified by the given id
	 @throws Exception
	 */
	static List<Map<String, Object>> resolve(final String id, final ClassLoader clsLoader)
	throws Exception {
		final ServiceLoader<SchemaProvider> loader = ServiceLoader.load(
			SchemaProvider.class, clsLoader
		);
		final List<Map<String, Object>> matchingSchemes = new ArrayList<>();
		for(final SchemaProvider schemaProvider : loader) {
			if(id.equals(schemaProvider.id())) {
				matchingSchemes.add(schemaProvider.schema());
			}
		}
		return matchingSchemes;
	}

	/**
	 @param id schema id
	 @param clsLoader clsLoader the class loader which will be used for the resolution
	 @return the single schema reduced from the multiple schemes resolved
	 @throws Exception
	 */
	static Map<String, Object> resolveAndReduce(final String id, final ClassLoader clsLoader)
	throws Exception {
		return reduceForest(resolve(id, clsLoader));
	}


}
