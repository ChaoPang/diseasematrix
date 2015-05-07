package org.molgenis.diseasematrix.mapping;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.molgenis.data.support.DefaultAttributeMetaData;
import org.molgenis.data.support.DefaultEntityMetaData;
import org.molgenis.util.ResourceUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoadDiseaseHpoMappingServiceImplTest
{
	LoadDiseaseHpoMappingServiceImpl loadDiseaseHpoMappingServiceImpl;

	@BeforeClass
	public void setup() throws IOException
	{
		File mappingFile = ResourceUtils.getFile("disease_hpo_mappings_sub.csv");
		loadDiseaseHpoMappingServiceImpl = new LoadDiseaseHpoMappingServiceImpl(mappingFile);

	}

	@Test
	public void attributeExists()
	{
		DefaultEntityMetaData entityMetaData = new DefaultEntityMetaData("test");
		entityMetaData.addAttributeMetaData(new DefaultAttributeMetaData("Disease"));
		entityMetaData.addAttributeMetaData(new DefaultAttributeMetaData("HPO"));
		entityMetaData.addAttributeMetaData(new DefaultAttributeMetaData("attribute 1"));
		entityMetaData.addAttributeMetaData(new DefaultAttributeMetaData("attribute 2"));

		assertTrue(loadDiseaseHpoMappingServiceImpl.attributeExists("Disease", entityMetaData));
		assertTrue(loadDiseaseHpoMappingServiceImpl.attributeExists("HPO", entityMetaData));
		assertTrue(loadDiseaseHpoMappingServiceImpl.attributeExists("attribute 1", entityMetaData));
		assertTrue(loadDiseaseHpoMappingServiceImpl.attributeExists("attribute 2", entityMetaData));
		assertFalse(loadDiseaseHpoMappingServiceImpl.attributeExists("attribute 3", entityMetaData));
	}

	@Test
	public void get()
	{
		assertEquals(
				loadDiseaseHpoMappingServiceImpl.get("OMIM:222470").toString(),
				"[HP:0002299, HP:0001561, HP:0000494, HP:0002014, HP:0002212, HP:0000154, HP:0000193, HP:0012023, HP:0001518, HP:0009891, HP:0011473, HP:0011031, HP:0000007, HP:0000369, HP:0006267, HP:0000520, HP:0002007, HP:0001399, HP:0000343, HP:0001732, HP:0000445, HP:0002224, HP:0001511, HP:0001396, HP:0001395, HP:0003235, HP:0004322, HP:0000463, HP:0002240, HP:0000160, HP:0001394, HP:0002041, HP:0003073, HP:0008070, HP:0100543, HP:0011220, HP:0008551, HP:0001508, HP:0002715, HP:0000457, HP:0000952, HP:0009886, HP:0000316, HP:0004734, HP:0002213]");

		assertEquals(
				loadDiseaseHpoMappingServiceImpl.get("OMIM:230650").toString(),
				"[HP:0003202, HP:0001332, HP:0001871, HP:0007759, HP:0004322, HP:0003651, HP:0002650, HP:0003274, HP:0001350, HP:0000271, HP:0002808, HP:0008430, HP:0000926, HP:0008166, HP:0000007, HP:0002869, HP:0002506, HP:0001256]");
	}

	@Test
	public void getAllDiseases()
	{
		assertEquals(loadDiseaseHpoMappingServiceImpl.getAllDiseases().size(), 127);
		assertEquals(
				loadDiseaseHpoMappingServiceImpl.getAllDiseases().toString(),
				"[OMIM:222470, OMIM:230650, OMIM:610448, OMIM:156400, OMIM:605249, OMIM:109560, OMIM:611228, OMIM:614751, OMIM:204300, OMIM:614833, OMIM:140700, ORPHANET:35099, OMIM:615972, OMIM:614507, OMIM:601072, OMIM:616084, OMIM:253270, OMIM:614565, OMIM:616099, OMIM:133190, OMIM:273750, OMIM:615160, OMIM:608978, OMIM:123500, OMIM:252600, OMIM:608390, OMIM:610629, OMIM:136900, OMIM:610293, OMIM:611938, OMIM:605074, ORPHANET:164736, OMIM:607634, OMIM:104510, OMIM:610965, OMIM:186830, OMIM:268150, OMIM:217000, OMIM:611307, OMIM:169100, OMIM:607174, OMIM:613581, OMIM:116100, OMIM:131300, OMIM:610445, OMIM:302045, ORPHANET:2345, OMIM:614025, ORPHANET:3250, OMIM:307000, OMIM:125853, OMIM:612075, OMIM:607734, OMIM:194380, OMIM:615085, OMIM:258870, ORPHANET:99798, OMIM:242600, ORPHANET:2704, OMIM:614885, OMIM:613809, OMIM:612109, OMIM:612304, OMIM:614867, OMIM:615065, OMIM:616032, OMIM:604537, OMIM:151430, OMIM:275200, OMIM:170400, OMIM:614940, OMIM:173200, OMIM:609180, OMIM:268100, OMIM:612877, OMIM:607101, OMIM:156200, OMIM:611091, OMIM:219150, OMIM:616248, OMIM:616005, OMIM:127750, ORPHANET:60030, OMIM:612651, OMIM:182940, ORPHANET:84090, OMIM:616051, OMIM:125350, OMIM:610127, OMIM:608649, OMIM:176270, OMIM:615510, OMIM:614915, OMIM:608643, OMIM:220120, OMIM:615092, OMIM:300755, OMIM:207750, OMIM:153600, OMIM:613908, OMIM:615006, OMIM:614878, ORPHANET:912, OMIM:611615, OMIM:614160, OMIM:608647, OMIM:259500, OMIM:260005, OMIM:114500, OMIM:615398, OMIM:615895, OMIM:615381, OMIM:600462, ORPHANET:461, OMIM:614959, OMIM:607115, OMIM:206800, OMIM:258100, OMIM:614429, OMIM:615505, OMIM:603776, OMIM:613856, OMIM:607371, OMIM:142310, OMIM:614458, OMIM:614474, OMIM:180860]");
	}
}
