package org.molgenis.diseasematrix.ontology;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.molgenis.ontology.utils.OntologyLoader;

abstract public class OntologyDistanceService
{
	protected final static Logger LOG = Logger.getLogger(LocalOntologyDistanceServiceImpl.class);
	protected final Map<String, Set<String>> nodePathMapping = new HashMap<String, Set<String>>();
	private final LocalOntologyServiceHelper helper = new LocalOntologyServiceHelper();

	abstract void loadOntologyTermNodePaths(OntologyLoader ontologyLoader);

	public int calculateDistance(String ontologyTermIdentifier1, String ontologyTermIdentifier2)
	{
		if (!nodePathMapping.containsKey(ontologyTermIdentifier1))
		{
			LOG.error("OntologyTerm : " + ontologyTermIdentifier1 + " does not exist!");
			return -1;
		}

		if (!nodePathMapping.containsKey(ontologyTermIdentifier2))
		{
			LOG.error("OntologyTerm : " + ontologyTermIdentifier2 + " does not exist!");
			return -1;
		}

		return helper.calculateMinimalDistance(nodePathMapping.get(ontologyTermIdentifier1),
				nodePathMapping.get(ontologyTermIdentifier2));
	}

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
		return helper.getNodePathsDistance(nodePath1, nodePath2);
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
		return helper.constructNodePath(parentNodePath, currentPosition);
	}
}
