package org.molgenis.diseasematrix.mapping;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.molgenis.data.Entity;
import org.molgenis.data.EntityMetaData;
import org.molgenis.data.csv.CsvRepository;

public class LoadDiseaseHpoMappingServiceImpl implements LoadMappingService
{
	private final Map<String, Set<String>> mappings = new HashMap<String, Set<String>>();
	private final static List<String> COLUMN_HEADERS = Arrays.asList("Disease", "HPO");

	public LoadDiseaseHpoMappingServiceImpl(File file) throws IOException
	{
		load(file);
	}

	public void load(File file) throws IOException
	{
		CsvRepository csvRepository = null;
		try
		{
			csvRepository = new CsvRepository(file, Collections.emptyList(), ';');
			EntityMetaData entityMetaData = csvRepository.getEntityMetaData();

			if (COLUMN_HEADERS.stream().allMatch(attributeName -> attributeExists(attributeName, entityMetaData)))
			{
				Iterator<Entity> iterator = csvRepository.iterator();

				while (iterator.hasNext())
				{
					Entity entity = iterator.next();
					String diseaseName = entity.getString(COLUMN_HEADERS.get(0));
					String phenotype = entity.getString(COLUMN_HEADERS.get(1));

					if (!mappings.containsKey(diseaseName))
					{
						mappings.put(diseaseName, new HashSet<String>());
					}
					mappings.get(diseaseName).add(phenotype);
				}
			}
		}
		finally
		{
			csvRepository.close();
		}
	}

	public Set<String> get(String identifier)
	{
		return mappings.containsKey(identifier) ? mappings.get(identifier) : Collections.emptySet();
	}

	public boolean attributeExists(String attributeName, EntityMetaData entityMetaData)
	{
		return entityMetaData.getAttribute(attributeName) != null;
	}

	@Override
	public Set<String> getAllDiseases()
	{
		return mappings.keySet();
	}
}