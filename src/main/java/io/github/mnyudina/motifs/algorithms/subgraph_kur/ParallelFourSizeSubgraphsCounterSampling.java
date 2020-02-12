package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import io.github.mnyudina.motifs.algorithms.GraphStatsOperation;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * This is parallel version of 4-size undirected subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Andrey Kurchanov
 */
public class ParallelFourSizeSubgraphsCounterSampling<V, E> implements GraphStatsOperation {

	private Graph<V, E> graph;

    private int numberOfRuns;

    private int numberOfThreads;
    
    private long exploredNumberOfSubgraphs4_1, exploredNumberOfSubgraphs4_2, exploredNumberOfSubgraphs4_3, exploredNumberOfSubgraphs4_4, exploredNumberOfSubgraphs4_5, exploredNumberOfSubgraphs4_6;
    
    private int approximateNumberOfSubgraphs4_1, approximateNumberOfSubgraphs4_2, approximateNumberOfSubgraphs4_3, approximateNumberOfSubgraphs4_4, approximateNumberOfSubgraphs4_5, approximateNumberOfSubgraphs4_6;
    
    List<Integer> resultsList;
    
	/**
	 * This nested static class is used to store parameters of each layer of the
	 * vertices such as probability of selection and list of vertices.
	 * 
	 * @author Andrey Kurchanov
	 */
    protected static class VertexLayerParameters<V> {
		
		private double probability;
		
		private List<V> vertices = new ArrayList<>();
		
		public VertexLayerParameters() {
			vertices = new ArrayList<>();
		}
		
		public double getProbability() {
			return probability;
		}

		public List<V> getVerticies() {
			return vertices;
		}
		
	}
    
	/**
	 * This nested static class is used to store parameters of each layer of the
	 * edges such as probability of selection and list of edges.
	 * 
	 * @author Andrey Kurchanov
	 */
    protected static class EdgeLayerParameters<E> {
		
		private double probability;
		
		private List<E> edges = new ArrayList<>();
		
		public EdgeLayerParameters() {
			edges = new ArrayList<>();
		}
		
		public double getProbability() {
			return probability;
		}

		public List<E> getEdges() {
			return edges;
		}
		
	}
	
	/**
     * Constructs and initializes the class.
     *
     * @author Andrey Kurchanov
     * @param graph the graph
     * @param numberOfRuns number of runs of sampling algorithm 
     * @param numberOfThreads number of parallel threads
     */
	public ParallelFourSizeSubgraphsCounterSampling(Hypergraph<V, E> graph, int numberOfRuns, int numberOfThreads) {
        this.graph = (Graph<V, E>) graph;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves number of explored <code>graph</code>'s subgraphs4_1 into
	 * <code>exploredNumberOfSubgraphs4_1</code> variable.<br>
	 * Saves results of runs of the algorithm into <code>resultsList</code>.<br>
	 * If the algorithm returns 6, than subgraph4_6 was explored.<br>
	 * If the algorithm returns 5, than subgraph4_5 was explored.<br>
	 * If the algorithm returns 4, than subgraph4_4 was explored.<br>
	 * If the algorithm returns 3, than subgraph4_3 was explored.<br>
	 * If the algorithm returns 2, than subgraph4_2 was explored.<br>
	 * After the results of all runs were obtained:<br>
	 * Number of explored <code>graph</code>'s subgraphs4_2 is saved into
	 * <code>exploredNumberOfSubgraphs4_2</code> variable.<br>
	 * Number of explored <code>graph</code>'s subgraphs4_3 is saved into
	 * <code>exploredNumberOfSubgraphs4_3</code> variable.<br>
	 * Number of explored <code>graph</code>'s subgraphs4_4 is saved into
	 * <code>exploredNumberOfSubgraphs4_4</code> variable.<br>
	 * Number of explored <code>graph</code>'s subgraphs4_5 is saved into
	 * <code>exploredNumberOfSubgraphs4_5</code> variable.<br>
	 * Number of explored <code>graph</code>'s subgraphs4_6 is saved into
	 * <code>exploredNumberOfSubgraphs4_6</code> variable.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_1 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_1 divided by number
	 * of runs of sampling algorithm and multiplied by exact number of the
	 * <code>graph</code>'s subgraphs4_1.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_2 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_2 divided by number
	 * of runs of sampling algorithm and multiplied by exact number of the
	 * <code>graph</code>'s path of length three.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_3 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_3 divided by twice
	 * number of runs of sampling algorithm and multiplied by exact number of
	 * the <code>graph</code>'s path of length three.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_4 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_4 divided by four
	 * times number of runs of sampling algorithm and multiplied by exact number
	 * of the <code>graph</code>'s path of length three.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_5 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_5 divided by six
	 * times number of runs of sampling algorithm and multiplied by exact number
	 * of the <code>graph</code>'s path of length three.<br>
	 * Approximate number of the <code>graph</code>'s subgraphs4_6 is calculated
	 * as number of explored <code>graph</code>'s subgraphs4_6 divided by twelve
	 * times number of runs of sampling algorithm and multiplied by exact number
	 * of the <code>graph</code>'s path of length three.<br>
	 * If the <code>graph</code> includes directed edges then
	 * <code>com.asoiu.simbigraph.exception.UnsupportedEdgeTypeException</code>
	 * is thrown.
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Andrey Kurchanov
	 * @throws UnsupportedEdgeTypeException
	 */
	@Override
	public void execute() throws GraphStatsException {
		if (graph.getDefaultEdgeType() == EdgeType.DIRECTED) {
			throw new UnsupportedEdgeTypeException("The parallel version of 4-size subgraphs counter which uses random carcasses sampling algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
		Collection<V> vertices = graph.getVertices();
		Map<Integer, VertexLayerParameters<V>> vertexLayers = new HashMap<>();
		int numberOfVertexSuccessors;
		
		/* 
		 * Bind each vertex of the graph to one layer of the vertices
		 * defined by number of successors of the vertex.
		 */
		for (V vertex : vertices) {
    		numberOfVertexSuccessors = graph.getSuccessors(vertex).size();
    		if (vertexLayers.get(numberOfVertexSuccessors) == null) {
    			vertexLayers.put(numberOfVertexSuccessors, new VertexLayerParameters<>());
    		}
    		vertexLayers.get(numberOfVertexSuccessors).vertices.add(vertex);
		}
		
		int exactNumberOfSubgraphs4_1 = 0;
		// Calculate exact number of the graph's subgraphs4_1
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			exactNumberOfSubgraphs4_1 += vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1) * (vertexLayer.getKey() - 2) / 6;
		}
		
		// Calculate probability of selection for each layer of the vertices
    	for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
    		vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1) * (vertexLayer.getKey() - 2)) / (6.0 * exactNumberOfSubgraphs4_1);
    	}

    	Collection<E> edges = graph.getEdges();
		Map<Integer, EdgeLayerParameters<E>> edgeLayers = new HashMap<>();
		int numberOfPathsOfLengthThree;
		
		/* 
		 * Bind each edge of the graph to one layer of the edges
		 * defined by number of path of length 3 including the edge.
		 */
		for (E edge : edges) {
			V v1 = graph.getEndpoints(edge).getFirst();
			V v2 = graph.getEndpoints(edge).getSecond();
			numberOfPathsOfLengthThree = (graph.getSuccessors(v1).size() - 1) * (graph.getSuccessors(v2).size() - 1);
			if (edgeLayers.get(numberOfPathsOfLengthThree) == null) {
				edgeLayers.put(numberOfPathsOfLengthThree, new EdgeLayerParameters<>());
    		}
			edgeLayers.get(numberOfPathsOfLengthThree).edges.add(edge);
		}
		
		int exactNumberOfPathsOfLengthThree = 0;
		// Calculate exact number of the graph's path of length three
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			exactNumberOfPathsOfLengthThree += edgeLayer.getValue().edges.size() * edgeLayer.getKey();
		}
		
		// Calculate probability of selection for each layer of the edges
    	for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
    		edgeLayer.getValue().probability = (edgeLayer.getValue().edges.size() * edgeLayer.getKey()) / (double)exactNumberOfPathsOfLengthThree;
    	}
    	
    	FourSizeSubgraphsCounterSampling<V, E> counter = new FourSizeSubgraphsCounterSampling<>(graph, vertexLayers, edgeLayers);
    	List<Integer> resultsOfRuns = new ArrayList<>(numberOfRuns);
    	for (int i = 0; i < numberOfRuns; i++) {
    		resultsOfRuns.add(0);
		}

    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_1 = resultsOfRuns.stream().parallel().mapToInt(resultOfRun -> counter.searchSubgraphs4_1()).sum()).get();
			forkJoinPool.submit(() -> resultsList = resultsOfRuns.stream().parallel().map(resultOfRun -> counter.searchOtherTypesOfSubgraphs()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_6 = resultsList.stream().parallel().filter(result -> result == 6).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_5 = resultsList.stream().parallel().filter(result -> result == 5).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_4 = resultsList.stream().parallel().filter(result -> result == 4).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_3 = resultsList.stream().parallel().filter(result -> result == 3).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs4_2 = resultsList.stream().parallel().filter(result -> result == 2).count()).get();
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		approximateNumberOfSubgraphs4_1 = (int) ((double)exploredNumberOfSubgraphs4_1 / numberOfRuns * exactNumberOfSubgraphs4_1);
		approximateNumberOfSubgraphs4_2 = (int) ((double)exploredNumberOfSubgraphs4_2 / numberOfRuns * exactNumberOfPathsOfLengthThree);
		approximateNumberOfSubgraphs4_3 = (int) ((double)exploredNumberOfSubgraphs4_3 / numberOfRuns * exactNumberOfPathsOfLengthThree / 2);
		approximateNumberOfSubgraphs4_4 = (int) ((double)exploredNumberOfSubgraphs4_4 / numberOfRuns * exactNumberOfPathsOfLengthThree / 4);
		approximateNumberOfSubgraphs4_5 = (int) ((double)exploredNumberOfSubgraphs4_5 / numberOfRuns * exactNumberOfPathsOfLengthThree / 6);
		approximateNumberOfSubgraphs4_6 = (int) ((double)exploredNumberOfSubgraphs4_6 / numberOfRuns * exactNumberOfPathsOfLengthThree / 12);
	}
	
	/**
	 * @author Andrey Kurchanov
	 */
	@Override
    public String toString() {
		int approximateNumberOfSubgraphs = approximateNumberOfSubgraphs4_1 + approximateNumberOfSubgraphs4_2 + approximateNumberOfSubgraphs4_3 + approximateNumberOfSubgraphs4_4 + approximateNumberOfSubgraphs4_5 + approximateNumberOfSubgraphs4_6;
    	return String.format("Number of subgraphs4_1 = %d(%.3f%%). "
    					    +"Number of subgraphs4_2 = %d(%.3f%%). "
    					    +"Number of subgraphs4_3 = %d(%.3f%%). "
    					    +"Number of subgraphs4_4 = %d(%.3f%%). "
    					    +"Number of subgraphs4_5 = %d(%.3f%%). "
    					    +"Number of subgraphs4_6 = %d(%.3f%%).",
    					    approximateNumberOfSubgraphs4_1, (double)approximateNumberOfSubgraphs4_1 / approximateNumberOfSubgraphs * 100.0,
    					    approximateNumberOfSubgraphs4_2, (double)approximateNumberOfSubgraphs4_2 / approximateNumberOfSubgraphs * 100.0,
    					    approximateNumberOfSubgraphs4_3, (double)approximateNumberOfSubgraphs4_3 / approximateNumberOfSubgraphs * 100.0,
    					    approximateNumberOfSubgraphs4_4, (double)approximateNumberOfSubgraphs4_4 / approximateNumberOfSubgraphs * 100.0,
    					    approximateNumberOfSubgraphs4_5, (double)approximateNumberOfSubgraphs4_5 / approximateNumberOfSubgraphs * 100.0,
    					    approximateNumberOfSubgraphs4_6, (double)approximateNumberOfSubgraphs4_6 / approximateNumberOfSubgraphs * 100.0);
    }

}