package org.molgenis.diseasematrix.mapping;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface LoadMappingService
{
	/**
	 * Load a list of mappings
	 * 
	 * @param file
	 */
	abstract void load(File file) throws IOException;

	/**
	 * Get a list of mappings based on the given identifier
	 * 
	 * @param identifier
	 * @return list of mappings
	 */
	abstract Set<String> get(String identifier);

	/**
	 * Get all diseases
	 * 
	 * @return
	 */
	abstract Set<String> getAllDiseases();
}
