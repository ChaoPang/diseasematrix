package org.molgenis.diseasematrix.ontology;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.ontology.utils.OWLClassContainer;
import org.molgenis.ontology.utils.OntologyLoader;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.google.common.collect.TreeTraverser;

public class LocalOntologyDistanceServiceImpl extends OntologyDistanceService
{
	private static final String PSEUDO_ROOT_CLASS_NODEPATH = "0[0]";
	private final static String PSEUDO_ROOT_CLASS_LABEL = "top";

	public LocalOntologyDistanceServiceImpl(File ontologyFile) throws OWLOntologyCreationException
	{
		LOG.info("start loading ontology : " + ontologyFile.getName());
		OntologyLoader ontologyLoader = new OntologyLoader(ontologyFile.getName(), ontologyFile);
		LOG.info("ontology : " + ontologyFile.getName() + " has been loaded");

		LOG.info("start loading node path for ontology terms");
		loadOntologyTermNodePaths(ontologyLoader);
		LOG.info("all node paths have been loaded");
	}

	public void loadOntologyTermNodePaths(final OntologyLoader ontologyLoader)
	{
		TreeTraverser<OWLClassContainer> traverser = new TreeTraverser<OWLClassContainer>()
		{
			public Iterable<OWLClassContainer> children(OWLClassContainer container)
			{
				int count = 0;
				List<OWLClassContainer> containers = new ArrayList<OWLClassContainer>();
				for (OWLClass childClass : ontologyLoader.getChildClass(container.getOwlClass()))
				{
					containers.add(new OWLClassContainer(childClass, constructNodePath(container.getNodePath(), count),
							false));
					count++;
				}
				return containers;
			}
		};

		OWLClass pseudoRootClass = ontologyLoader.createClass(PSEUDO_ROOT_CLASS_LABEL, ontologyLoader.getRootClasses());

		Iterator<OWLClassContainer> iterator = traverser.preOrderTraversal(
				new OWLClassContainer(pseudoRootClass, PSEUDO_ROOT_CLASS_NODEPATH, true)).iterator();

		while (iterator.hasNext())
		{
			OWLClassContainer container = iterator.next();
			OWLClass ontologyTerm = container.getOwlClass();
			if (!StringUtils.equals(ontologyLoader.getLabel(ontologyTerm), PSEUDO_ROOT_CLASS_LABEL))
			{
				String id = ontologyLoader.getId(ontologyTerm);
				String ontologyTermNodePath = container.getNodePath();
				if (!nodePathMapping.containsKey(id))
				{
					nodePathMapping.put(id, new HashSet<String>());
				}
				nodePathMapping.get(id).add(ontologyTermNodePath);
			}
		}
	}
}