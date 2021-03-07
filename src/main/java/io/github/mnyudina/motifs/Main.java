package io.github.mnyudina.motifs;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import io.github.mnyudina.motifs.algorithms.GraphStatsOperation;
import io.github.mnyudina.motifs.algorithms.subgraph.*;
import io.github.mnyudina.motifs.algorithms.subgraph_myold.RandMSF4DirOpt;
import io.github.mnyudina.motifs.algorithms.subgraph_myold.RandSF4_old;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.graph.MyDirectedSparseGraph;
import io.github.mnyudina.motifs.graph.MySparseGraph;
import io.github.mnyudina.motifs.util.ArgumentParser;
import io.github.mnyudina.motifs.util.FormatUtils;
import io.github.mnyudina.motifs.util.ProgramParameters;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetReader;

import org.apache.commons.cli.ParseException;
import org.apache.commons.collections15.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

/**
 * @author Gleepa
 */
public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class.getName());

    private static ProgramParameters parameters;

    /**
     * This is entry point in the program.
     * 
     * @author Gleepa
     * @param args input arguments
     */
    public static void main11(String[] args) {
    	String inputArgs = "";
    	
    	for (String string : args) {
    		inputArgs += string + " ";
		}
    	
    	LOG.debug("Input arguments: " + inputArgs);

        long startTime;
        Graph<Integer, Integer> graph;

        ProgramParameters parameters = parseCmd(args);
        graph = initGraph();

        if (graph.getVertexCount() == 0) {
            LOG.error("Graph is empty.");
            System.exit(1);
        }
        LOG.info("Vertices = {}.", graph.getVertexCount());
        LOG.info("{} = {}.", graph.getDefaultEdgeType() == EdgeType.DIRECTED ? "Arcs" : "Edges", graph.getEdgeCount());
        
        List<GraphStatsOperation> requestedOperation = new ArrayList<GraphStatsOperation>();


        if (parameters.getIsThreeSizeSubgraphsCountFullEnumerationRequestedFlag()) {
            // TO DO
        }
        if (parameters.getIsThreeSizeSubgraphsCountSamplingRequestedFlag()) {
            requestedOperation.add(new RandFS3<>(graph, parameters.getNumberOfRuns(),true));
        }
        if (parameters.getIsFourSizeSubgraphsCountFullEnumerationRequestedFlag()) {
            // TO DO
        }
        if (parameters.getIsFourSizeSubgraphsCountSamplingRequestedFlag()) {
            //requestedOperation.add(new RandSF4<>(graph, parameters.getNumberOfRuns()));
            requestedOperation.add(new RandSF4_streams<>(graph, parameters.getNumberOfRuns()));
        }



        if (requestedOperation.isEmpty()) {
        	LOG.warn("No one of available operations has been requested.");
        } else {
	        for (GraphStatsOperation graphStatsOperation : requestedOperation) {
	        	startTime = System.nanoTime();
	        	try {
					graphStatsOperation.execute();
					LOG.info(graphStatsOperation);
				} catch (GraphStatsException e) {
					LOG.error(e.getMessage());
					LOG.debug(e);
				}
	        	LOG.info("Elapsed time = {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
			}
        }
    }
    
	/**
	 * This is a wrapper for <code>parseCmdParameters(String[])</code> method of
	 * the <code>io.github.mnyudina.motifs.util.ArgumentParser</code> instance.
	 * 
	 * @author Gleepa
	 * @param args input arguments
	 * @see ArgumentParser#parseCmdParameters(String[])
	 * @return the instance of <code>com.asoiu.simbigraph.util.ProgramParameters</code> if
	 *         input arguments were parsed successfully, otherwise thrown
	 *         exception is logged and the program shuts down
	 */
    private static ProgramParameters parseCmd(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        try {
            parameters = parser.parseCmdParameters(args);
        } catch (ParseException | NumberFormatException e) {
            LOG.error("Can't parse cmd parameters.");
            LOG.debug("Can't parse cmd parameters.", e);
            System.exit(1);
        }
        return parameters;
    }

	/**
	 * This is a wrapper for <code>loadGraph(String)</code> method.
	 * 
	 * @author Gleepa
	 * @see Main#loadGraph(String)
	 * @return the instance of a class which implements
	 *         <code>edu.uci.ics.jung.graph.Hypergraph</code> interface if
	 *         specific graph was loaded successfully, otherwise thrown
	 *         exception is logged and the program shuts down
	 */
    private static Graph<Integer, Integer> initGraph() {
        long startTime;
        Graph<Integer, Integer> graph = null;

        LOG.info("Loading graph from {} file.", parameters.getGraphFile());
        startTime = System.nanoTime();
        try {
            graph = loadGraph(parameters.getGraphFile());
            LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
        } catch (IOException e) {
        	LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
            LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
            System.exit(1);
        }



        return graph;
    }

	/**
	 * Loads information about specific graph by using
	 * <code>load(String, edu.uci.ics.jung.graph.Graph)</code> method of
	 * <code>edu.uci.ics.jung.io.PajekNetReader</code> instance.
	 * 
	 * @author Gleepa
	 * @param path a string representation of the path to the graph file
	 * @throws IOException
	 * @see edu.uci.ics.jung.io.PajekNetReader#load(String, edu.uci.ics.jung.graph.Graph)
	 * @return the instance of a class which implements
	 *         <code>edu.uci.ics.jung.graph.Hypergraph</code> interface if
	 *         specific graph was loaded successfully, otherwise
	 *         <code>IOException</code> is thrown
	 */
    private static Graph loadGraph(String path) throws IOException {

        //!FIX HERE myDirectedSparseGraph-------------------------------------------------
       // return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MyDirectedSparseGraph<>());//AdjacencyListGraph
        return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph

    }

    /**
     * @author Gleepa
     * @return the factory object
     */
    private static Factory<Integer> createIntegerFactory() {
        return new Factory<Integer>() {
            private int n = 0;

            @Override
            public Integer create() {
                return n++;
            }
        };
    }

    public static Graph initGraphEdgelistLong(String filename) {
        long startTime;
        Graph<String, Long> graph = null;

        //LOG.info("Loading graph from {} file.", parameters.getGraphFile());
        startTime = System.nanoTime();
        try {
            graph = loadGraphLongEdgeList(filename);
            LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
        } catch (IOException e) {
            LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
            LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
            System.exit(1);
        }
        return graph;
    }
    private static Graph<String, Long> loadGraphLongEdgeList(String path) throws IOException {
        BufferedReader br = null;
        Graph<String, Long> gr = new MySparseGraph<>();
        String sCurrentLine;
            br = new BufferedReader(new FileReader(path));
            int i=0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] strMass=sCurrentLine.split(" ");
                if(strMass.length==2){
                    gr.addEdge(new Long(i++),(strMass[0]), (strMass[1]),EdgeType.DIRECTED);
                }
                else throw new IOException("Wrong file format ?");
            }
        return gr;
    }
    //_DEBUG
    public static void main(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        String path="graphs\\PathwayCommons.net";
       // String path="graphs\\my_polBlog.net";
      //  String path="graphs\\GenReg.net";
     //   String path="graphs\\MyAs.net";

     //   graph= new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MyDirectedSparseGraph<>());//AdjacencyListGraph
      // graph=new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph
        graph = initGraphEdgelistLong("graphs\\G+.txt");

        System.out.println(graph.getEdgeCount());
        System.out.println(graph.getVertexCount());
        System.out.println(graph);


        long startTime = System.nanoTime();
       // RandMSF4DirOpt p4= new RandMSF4DirOpt<>(graph,10000,true);
      //  RandSF4_old p4= new RandSF4_old<>(graph,10000);
        RandSF4 p4= new RandSF4<>(graph,100000);
      // RandSF4_new2 p4= new RandSF4_new2<>(graph,10000);
      // RandSF4_new p4= new RandSF4_new<>(graph,1000);
       // RandMSF4Dir p4= new RandMSF4Dir<>(graph,1000);


        long t1=System.currentTimeMillis();
        p4.execute();
        System.out.println("dt="+FormatUtils.durationToHMS(System.nanoTime() - startTime));
        System.out.println(p4.toString());


    }

    public static void main3(String[] args) throws IOException {
        Graph graph;
        //graph = loadGraph("graphs\\my_polBlog.net");
        graph = loadGraph("graphs\\PathwayCommons.net");

        /*graph= new MySparseGraph<>();
        graph.addEdge(1,1,2,EdgeType.DIRECTED);
        graph.addEdge(4,2,1,EdgeType.DIRECTED);
        graph.addEdge(2,1,3,EdgeType.DIRECTED);
        graph.addEdge(3,2,3,EdgeType.DIRECTED);
*/
        System.out.println(graph.getEdgeCount());
        System.out.println(graph.getVertexCount());
      //  System.out.println(graph);



    }




}