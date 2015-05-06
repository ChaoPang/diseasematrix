package org.molgenis.diseasematrix.matrix;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.molgenis.data.csv.CsvWriter;
import org.molgenis.diseasematrix.mapping.LoadMappingService;
import org.molgenis.diseasematrix.ontology.LocalOntologyDistanceServiceImpl;
import org.molgenis.diseasematrix.ontology.OntologyDistanceService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

public class GenerateDistanceMatrixTest
{
	GenerateDistanceMatrix generateDistanceMatrix;

	@BeforeClass
	public void setup()
	{
		LoadMappingService loadMappingService = mock(LoadMappingService.class);

		when(loadMappingService.get("disease:1")).thenReturn(Sets.newHashSet("hp:1", "hp:2", "hp:3"));
		when(loadMappingService.get("disease:2")).thenReturn(Sets.newHashSet("hp:4", "hp:5", "hp:6"));

		OntologyDistanceService ontologyDistanceService = mock(LocalOntologyDistanceServiceImpl.class);
		when(ontologyDistanceService.calculateDistance("hp:1", "hp:4")).thenReturn(10);
		when(ontologyDistanceService.calculateDistance("hp:1", "hp:5")).thenReturn(5);
		when(ontologyDistanceService.calculateDistance("hp:1", "hp:6")).thenReturn(6);

		when(ontologyDistanceService.calculateDistance("hp:2", "hp:4")).thenReturn(20);
		when(ontologyDistanceService.calculateDistance("hp:2", "hp:5")).thenReturn(5);
		when(ontologyDistanceService.calculateDistance("hp:2", "hp:6")).thenReturn(1);

		when(ontologyDistanceService.calculateDistance("hp:3", "hp:4")).thenReturn(2);
		when(ontologyDistanceService.calculateDistance("hp:3", "hp:5")).thenReturn(3);
		when(ontologyDistanceService.calculateDistance("hp:3", "hp:6")).thenReturn(4);

		generateDistanceMatrix = new GenerateDistanceMatrix(loadMappingService, ontologyDistanceService,
				mock(CsvWriter.class));
	}

	@Test
	public void calculateDiseaseDistance()
	{
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		double average = (double) (10 + 5 + 6 + 20 + 5 + 1 + 2 + 3 + 4) / 9;
		assertEquals(generateDistanceMatrix.calculateDiseaseDistance("disease:1", "disease:2"),
				Double.parseDouble(decimalFormat.format(average)));
	}

	@Test
	public void calculateMean()
	{
		assertEquals(generateDistanceMatrix.calculateMean(Arrays.asList(1, 2, 3, 4)), 2.5);

		assertEquals(generateDistanceMatrix.calculateMean(Arrays.asList(1, 2, 3, 4, 5)), 3.0);

		assertEquals(generateDistanceMatrix.calculateMean(Arrays.asList(10, 1, 2, 7, 5, 21)), 7.67);
	}
}
