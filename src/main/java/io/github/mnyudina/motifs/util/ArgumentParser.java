package io.github.mnyudina.motifs.util;

import org.apache.commons.cli.*;

/**
 * @author Andrey Kurchanov, Gleepa
 */
public class ArgumentParser {

	private final Option graphPath;
    
    private final Option operations;
    
    private final Option numberOfRuns;

       private final Options options;

    private final HelpFormatter help;

    public ArgumentParser() {
    	graphPath = OptionBuilder
    		.withType(String.class)
    		.isRequired(true)
    		.hasArg(true)
    		.withLongOpt("file")
    		.withDescription("This mandatory parameter sets path to the graph file.")
    		.create("f")
    	;
    	operations = OptionBuilder
    		.withType(String.class)
    		.isRequired(true)
    		.hasArg(true)
    		.withLongOpt("operations")
    		.withDescription("List of available operations:\n"
    													  +"3scfe - get number of 3-size directed subgraphs by full enumeration algorithm,\n"
    													  +"3scs - get number of 3-size directed subgraphs by sampling algorithm,\n"
    													  +"4scfe - get number of 4-size directed subgraphs by full enumeration algorithm,\n"
    													  +"4scs - get number of 4-size directed subgraphs by sampling algorithm\nseparated by comma.")
    		.create("op")
    	;
    	numberOfRuns = OptionBuilder
        	.withType(Integer.class)
        	.isRequired(false)
        	.hasArg(true)
        	.withLongOpt("runs")
        	.withDescription("This parameter sets number of runs used by sampling algorithms only.")
        	.create("r")
    	;
    	options = new Options()
    		.addOption(graphPath)
    		.addOption(operations)
    		.addOption(numberOfRuns)
    	;
    	help = new HelpFormatter();
    }

	/**
	 * Parses input <code>args</code> and returns the instance of
	 * <code>io.github.mnyudina.ProgramParameters</code>.
	 *
	 * @author Andrey Kurchanov, Gleepa
	 * @param args input arguments
	 * @return the the instance of
	 *         <code>io.github.mnyudina.ProgramParameters</code> if input
	 *         arguments were parsed successfully, otherwise
	 *         <code>org.apache.commons.cli.ParseException</code> or
	 *         <code>NumberFormatException</code> is thrown
	 */
    public ProgramParameters parseCmdParameters(final String[] args) throws ParseException, NumberFormatException {
    	ProgramParameters parameters = new ProgramParameters();
        try {
            final CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(graphPath.getOpt())) {
               parameters.setGraphFile(cmd.getOptionValue(graphPath.getOpt()));
            }
            if (cmd.hasOption(operations.getOpt())) {
            	for (String string : cmd.getOptionValue(operations.getOpt()).split(",")) {
					switch (string) {
					case "3scfe":
						parameters.setIsThreeSizeSubgraphsCountFullEnumerationRequestedFlag();
						break;
					case "3scs":
						parameters.setIsThreeSizeSubgraphsCountSamplingRequestedFlag();
						break;
					case "4scfe":
						parameters.setIsFourSizeSubgraphsCountFullEnumerationRequestedFlag();
						break;
					case "4scs":
						parameters.setIsFourSizeSubgraphsCountSamplingRequestedFlag();
						break;
					default:
						break;
					}
				}
            }
            if (parameters.getIsThreeSizeSubgraphsCountSamplingRequestedFlag() || parameters.getIsFourSizeSubgraphsCountSamplingRequestedFlag()) {
            	if (cmd.hasOption(numberOfRuns.getOpt())) {
            		parameters.setNumberOfRuns(Integer.parseInt(cmd.getOptionValue(numberOfRuns.getOpt())));
            	} else {
            		throw new ParseException("Missing required by sampling algorithm option: " + numberOfRuns.getOpt());
				}
            }
        } catch (ParseException e) {
            help.printHelp("RFS", options);
            throw new ParseException(e.getMessage());
        }
        return parameters;
    }
    
}