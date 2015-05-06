package org.molgenis.diseasematrix.diseasematrix;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Hello world!
 *
 */
public class App
{
	public static void main(String[] args) throws ParseException, IOException, OWLOntologyCreationException
	{
		Options options = createOptions();
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("mapping") && cmd.hasOption("ontology") && cmd.hasOption("output"))
		{
			File mappingFile = new File(cmd.getOptionValue("mapping"));
			File ontologyFile = new File(cmd.getOptionValue("ontology"));
			File outputFile = new File(cmd.getOptionValue("output"));

			if (mappingFile.exists() && ontologyFile.exists() && outputFile.exists())
			{
				GenerateDistanceMatrix generateDistanceMatrix = new GenerateDistanceMatrix(mappingFile, ontologyFile,
						outputFile);
				generateDistanceMatrix.generate();
			}
		}
		else
		{
			showHelpMessage(options);
		}
	}

	private static void showHelpMessage(Options options)
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter
				.printHelp(
						"java -jar distance.jar",
						"where options include:",
						options,
						"\nTo calculate the distance matrix, it is suggested to increase the maximum amount of memory allocated to java e.g. -Xmx2G");
	}

	private static Options createOptions()
	{
		Options options = new Options();
		options.addOption(new Option("mapping", true, "provide the mapping file for diseases and hpo terms"));
		options.addOption(new Option("ontology", true, "provide the hpo ontology file"));
		options.addOption(new Option("output", true, "provide the path of output file"));
		return options;
	}
}
