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
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Andrey Kurchanov
 */
public class ParallelThreeSizeSubgraphsCounterSampling<V, E> implements GraphStatsOperation {

	private Hypergraph<V, E> graph;

    private int numberOfRuns;

    private int numberOfThreads;
    
    private int exploredNumberOfForks, exploredNumberOfTriangles;
    
    private int approximateNumberOfForks, approximateNumberOfTriangles;
    
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
     * Constructs and initializes the class.
     *
     * @author Andrey Kurchanov
     * @param graph the graph
     * @param numberOfRuns number of runs of sampling algorithm 
     * @param numberOfThreads number of parallel threads
     */
	public ParallelThreeSizeSubgraphsCounterSampling(Hypergraph<V, E> graph, int numberOfRuns, int numberOfThreads) {
        this.graph = graph;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves number of explored <code>graph</code>'s "forks" into
	 * <code>exploredNumberOfForks</code> variable.<br>
	 * Saves number of explored <code>graph</code>'s "triangles" into
	 * <code>exploredNumberOfTriangles</code> variable.<br>
	 * Approximate number of the <code>graph</code>'s "forks" is calculated as
	 * number of explored <code>graph</code>'s "forks" divided by number of runs
	 * of sampling algorithm and multiplied by exact number of the
	 * <code>graph</code>'s "forks".<br>
	 * Approximate number of the <code>graph</code>'s "triangles" is calculated
	 * as number of explored <code>graph</code>'s "triangles" divided by three
	 * times number of runs of sampling algorithm and multiplied by exact number
	 * of the <code>graph</code>'s "forks".<br>
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
	public void execute() throws UnsupportedEdgeTypeException {
		if (graph.getDefaultEdgeType() == EdgeType.DIRECTED) {
			throw new UnsupportedEdgeTypeException("The parallel version of 3-size subgraphs counter which uses random carcasses sampling algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
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
		
		int exactNumberOfForks = 0;
		// Calculate exact number of the graph's "forks"
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			exactNumberOfForks += vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1) / 2;
		}
		
		// Calculate probability of selection for each layer of the vertices
    	for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
    		vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1)) / (2.0 * exactNumberOfForks);
    	}
    	
    	ThreeSizeSubgraphsCounterSampling<V, E> counter = new ThreeSizeSubgraphsCounterSampling<>(graph, vertexLayers);
    	List<Integer> resultsOfRuns = new ArrayList<>(numberOfRuns);
    	for (int i = 0; i < numberOfRuns; i++) {
    		resultsOfRuns.add(0);
		}
    	
    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> exploredNumberOfTriangles = resultsOfRuns.stream().parallel().mapToInt(resultOfRun -> counter.doRun()).sum()).get();
			exploredNumberOfForks = numberOfRuns - exploredNumberOfTriangles;
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		approximateNumberOfForks = (int) ((double)exploredNumberOfForks / numberOfRuns * exactNumberOfForks);
		approximateNumberOfTriangles = (int) ((double)exploredNumberOfTriangles / numberOfRuns * exactNumberOfForks / 3);
	}
	
	/**
	 * @author Andrey Kurchanov
	 */
	@Override
    public String toString() {
		int approximateNumberOfSubgraphs = approximateNumberOfForks + approximateNumberOfTriangles;
    	return String.format("Number of forks = %d(%.3f%%). Number of triangles = %d(%.3f%%).", approximateNumberOfForks, (double)approximateNumberOfForks/approximateNumberOfSubgraphs * 100.0, approximateNumberOfTriangles, (double)approximateNumberOfTriangles/approximateNumberOfSubgraphs * 100.0);
    }

}
