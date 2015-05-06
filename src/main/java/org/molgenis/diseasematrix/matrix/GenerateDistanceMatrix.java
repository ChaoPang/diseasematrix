package org.molgenis.diseasematrix.matrix;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.data.csv.CsvWriter;
import org.molgenis.data.support.MapEntity;
import org.molgenis.diseasematrix.mapping.LoadDiseaseHpoMappingServiceImpl;
import org.molgenis.diseasematrix.mapping.LoadMappingService;
import org.molgenis.diseasematrix.ontology.LocalOntologyDistanceServiceImpl;
import org.molgenis.diseasematrix.ontology.OntologyDistanceService;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.beust.jcommander.internal.Lists;

public class GenerateDistanceMatrix
{
	private final LoadMappingService loadingMappingService;
	private final OntologyDistanceService ontologyDistanceService;
	private final CsvWriter csvWriter;
	private final DecimalFormat decimalFormat = new DecimalFormat("##.##");

	public GenerateDistanceMatrix(File mappingFile, File ontologyFile, File outputFile)
			throws OWLOntologyCreationException, IOException
	{
		this(new LoadDiseaseHpoMappingServiceImpl(mappingFile), new LocalOntologyDistanceServiceImpl(ontologyFile),
				new CsvWriter(outputFile));
	}

	public GenerateDistanceMatrix(LoadMappingService loadingMappingService,
			OntologyDistanceService ontologyDistanceService, CsvWriter csvWriter)
	{
		this.loadingMappingService = loadingMappingService;
		this.ontologyDistanceService = ontologyDistanceService;
		this.csvWriter = csvWriter;
	}

	public void generate() throws IOException
	{
		Set<String> diseases = loadingMappingService.getAllDiseases();

		List<String> csvAttributeNames = Lists.newArrayList(diseases);
		csvAttributeNames.add(0, "disease");
		csvWriter.writeAttributeNames(csvAttributeNames);

		for (String disease1 : diseases)
		{
			MapEntity mapEntity = new MapEntity();
			mapEntity.set("disease", disease1);
			for (String disease2 : diseases)
			{
				if (!StringUtils.equals(disease1, disease2))
				{
					mapEntity.set(disease2, calculateDiseaseDistance(disease1, disease2));
				}
				else
				{
					mapEntity.set(disease2, "N/A");
				}
			}
			csvWriter.add(mapEntity);
		}

		csvWriter.close();
	}

	public double calculateDiseaseDistance(String disease1, String disease2)
	{
		List<Integer> distances = new ArrayList<Integer>();

		for (String hpoTerm1 : loadingMappingService.get(disease1))
		{
			for (String hpoTerm2 : loadingMappingService.get(disease2))
			{
				distances.add(ontologyDistanceService.calculateDistance(hpoTerm1, hpoTerm2));
			}
		}

		return calculateMean(distances);
	}

	public double calculateMean(List<Integer> list)
	{
		double average = 0;
		for (Integer number : list)
		{
			average += number;
		}
		return Double.parseDouble(decimalFormat.format(average / list.size()));
	}
}