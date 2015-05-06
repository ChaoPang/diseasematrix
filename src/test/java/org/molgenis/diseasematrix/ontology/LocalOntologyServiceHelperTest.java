package org.molgenis.diseasematrix.ontology;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;

public class LocalOntologyServiceHelperTest
{
	LocalOntologyServiceHelper helper = new LocalOntologyServiceHelper();

	@Test
	public void getNodePathsDistance()
	{
		// Case 1
		assertEquals(helper.getNodePathsDistance("0[0].0[1]", "0[0].0[1].1[2]"), 1);

		// Case 2
		assertEquals(helper.getNodePathsDistance("0[0].0[1].1[2]", "0[0].0[1]"), 1);

		// Case 3
		assertEquals(helper.getNodePathsDistance("0[0].0[1].1[2].2[3]", "0[0].0[1].0[2].2[3]"), 4);

		// Case 4
		assertEquals(helper.getNodePathsDistance("0[0].0[1]", "0[0].0[1].0[2].1[3].2[4]"), 3);

		// Case 5
		assertEquals(helper.getNodePathsDistance("0[0].0[1]", "0[0].0[1]"), 0);
	}

	@Test
	public void constructNodePath()
	{
		assertEquals(helper.constructNodePath("0[0].0[1]", 1), "0[0].0[1].1[2]");
		assertEquals(helper.constructNodePath("0[0].0[1]", 2), "0[0].0[1].2[2]");
	}

	@Test
	public void calculateMinimalDistance()
	{
		assertEquals(
				helper.calculateMinimalDistance(Sets.newHashSet("0[0].0[1].1[2].2[3]"),
						Sets.newHashSet("0[0].0[1].0[2].2[3]")), 4);

		assertEquals(
				helper.calculateMinimalDistance(Sets.newHashSet("0[0].0[1].1[2].2[3]", "0[0].0[1].10[2].0[3]"),
						Sets.newHashSet("0[0].0[1].0[2].2[3]", "0[0].0[1].10[2].1[3]")), 2);
	}
}
