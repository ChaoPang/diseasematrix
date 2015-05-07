package org.molgenis.diseasematrix.matrix;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

		AtomicInteger totalNumber = new AtomicInteger();
		AtomicInteger progressNumber = new AtomicInteger();
		totalNumber.set(diseases.size());

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
					mapEntity.set(disease2, 0);
				}
			}
			csvWriter.add(mapEntity);

			printProgress(progressNumber.incrementAndGet(), totalNumber.intValue());
		}

		System.out.println("COMPLETE : " + totalNumber + " of diseases have been processed!");

		if (ontologyDistanceService.getUniqueErrorMessage().size() > 0)
		{
			ontologyDistanceService.getUniqueErrorMessage().forEach(error -> System.out.println(error));
		}

		csvWriter.close();
	}

	public void printProgress(int progressNumber, int totalNumber)
	{
		if (progressNumber % 100 == 0)
		{
			System.out.println(progressNumber + " out of " + totalNumber + " have been processed!");
		}
	}

	public double calculateDiseaseDistance(String disease1, String disease2)
	{
		List<Integer> distances = new ArrayList<Integer>();

		for (String hpoTerm1 : loadingMappingService.get(disease1))
		{
			for (String hpoTerm2 : loadingMappingService.get(disease2))
			{
				Integer calculateDistance = ontologyDistanceService.calculateDistance(hpoTerm1, hpoTerm2);
				if (calculateDistance != null)
				{
					distances.add(calculateDistance);
				}
			}
		}

		return calculateMean(distances);
	}

	public double calculateMean(List<Integer> list)
	{
		if (list.size() == 0) return 0;

		double average = 0;
		for (Integer number : list)
		{
			average += number;
		}
		return Double.parseDouble(decimalFormat.format(average / list.size()));
	}
}