package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import io.github.mnyudina.motifs.algorithms.GraphStatsOperation;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * full enumeration algorithm.
 * 
 * @author Andrey Kurchanov
 */
public class ParallelThreeSizeSubgraphsCounterFullEnumeration<V, E> implements GraphStatsOperation {

	private Hypergraph<V, E> graph;

    private int numberOfThreads;

    private int numberOfForks, numberOfTriangles;
	
    /**
     * Constructs and initializes the class.
     *
     * @author Andrey Kurchanov
     * @param graph the graph
     * @param numberOfThreads number of parallel threads
     */
	public ParallelThreeSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph, int numberOfThreads) {
        this.graph = graph;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves exact number of the <code>graph</code>'s "triangles"
	 * into <code>numberOfTriangles</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s "forks" into
	 * <code>numberOfForks</code> variable.<br>
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
			throw new UnsupportedEdgeTypeException("The parallel version of 3-size subgraphs counter which uses full enumeration algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
		ThreeSizeSubgraphsCounterFullEnumeration<V, E> counter = new ThreeSizeSubgraphsCounterFullEnumeration<>(graph);
		Collection<V> vertices = graph.getVertices();
    	
    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> numberOfTriangles = vertices.stream().parallel().mapToInt(vertex -> counter.getNumberOfTriangles(vertex)).sum() / 3).get();
			forkJoinPool.submit(() -> numberOfForks = vertices.stream().parallel().mapToInt(vertex -> counter.getNumberOfForks(vertex)).sum() - numberOfTriangles * 3).get();
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Andrey Kurchanov
	 */
	@Override
    public String toString() {
		int numberOfSubgraphs = numberOfForks + numberOfTriangles;		
    	return String.format("Number of forks = %d(%.3f%%). Number of triangles = %d(%.3f%%).", numberOfForks, (double)numberOfForks/numberOfSubgraphs * 100.0, numberOfTriangles, (double)numberOfTriangles/numberOfSubgraphs * 100.0);
    }

}