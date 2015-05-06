package org.molgenis.diseasematrix.ontology;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class LocalOntologyServiceHelper
{
	/**
	 * Calculate the distance between nodePaths, e.g. 0[0].1[1].2[2], 0[0].2[1].2[2]. The distance is the non-overlap
	 * part of the strings
	 * 
	 * @param nodePath1
	 * @param nodePath2
	 * @return distance
	 */
	public int getNodePathsDistance(String nodePath1, String nodePath2)
	{
		String[] nodePathFragment1 = nodePath1.split("\\.");
		String[] nodePathFragment2 = nodePath2.split("\\.");

		int overlapBlock = 0;
		while (overlapBlock < nodePathFragment1.length && overlapBlock < nodePathFragment2.length
				&& nodePathFragment1[overlapBlock].equals(nodePathFragment2[overlapBlock]))
		{
			overlapBlock++;
		}

		return nodePathFragment1.length + nodePathFragment2.length - overlapBlock * 2;
	}

	/**
	 * construct nodePath based on current position and parent nodePath
	 * 
	 * @param parentNodePath
	 * @param currentPosition
	 * @return
	 */
	public String constructNodePath(String parentNodePath, int currentPosition)
	{
		StringBuilder nodePathStringBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(parentNodePath))
		{
			nodePathStringBuilder.append(parentNodePath).append('.');
		}
		nodePathStringBuilder.append(currentPosition).append('[')
				.append(nodePathStringBuilder.toString().split("\\.").length - 1).append(']');
		return nodePathStringBuilder.toString();
	}

	/**
	 * calculate the minimal distance between two sets of nodePaths
	 * 
	 * @param firstNodePathSet
	 * @param secondNodePathSet
	 * @return minimal distance
	 */
	public int calculateMinimalDistance(Set<String> firstNodePathSet, Set<String> secondNodePathSet)
	{
		int distance = -1;
		for (String nodePath1 : firstNodePathSet)
		{
			for (String nodePath2 : secondNodePathSet)
			{
				int nodePathsDistance = getNodePathsDistance(nodePath1, nodePath2);
				if (distance == -1)
				{
					distance = nodePathsDistance;
				}
				else if (distance > nodePathsDistance)
				{
					distance = nodePathsDistance;
				}
			}
		}
		return distance;
	}
}
