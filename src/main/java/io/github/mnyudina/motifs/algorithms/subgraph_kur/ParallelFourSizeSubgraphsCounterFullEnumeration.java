package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import io.github.mnyudina.motifs.algorithms.GraphStatsOperation;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * This is parallel version of 4-size undirected subgraphs counter which uses
 * full enumeration algorithm.
 * 
 * @author Andrey Kurchanov
 */
public class ParallelFourSizeSubgraphsCounterFullEnumeration<V, E> implements GraphStatsOperation {

	private Graph<V, E> graph;

    private int numberOfThreads;
    
    private int numberOfSubgraphs4_1, numberOfSubgraphs4_2, numberOfSubgraphs4_3, numberOfSubgraphs4_4, numberOfSubgraphs4_5, numberOfSubgraphs4_6;
	
    /**
     * Constructs and initializes the class.
     *
     * @author Andrey Kurchanov
     * @param graph the graph
     * @param numberOfThreads number of parallel threads
     */
	public ParallelFourSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph, int numberOfThreads) {
        this.graph = (Graph<V, E>) graph;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves exact number of the <code>graph</code>'s subgraphs4_1
	 * into <code>numberOfSubgraphs4_1</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s subgraphs4_2
	 * into <code>numberOfSubgraphs4_2</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s subgraphs4_3
	 * into <code>numberOfSubgraphs4_3</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s subgraphs4_4
	 * into <code>numberOfSubgraphs4_4</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s subgraphs4_5
	 * into <code>numberOfSubgraphs4_5</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s subgraphs4_6
	 * into <code>numberOfSubgraphs4_6</code> variable.<br>
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
			throw new UnsupportedEdgeTypeException("The parallel version of 4-size subgraphs counter which uses full enumeration algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
		FourSizeSubgraphsCounterFullEnumeration<V, E> counter = new FourSizeSubgraphsCounterFullEnumeration<>(graph);
		Collection<V> vertices = graph.getVertices();
		Collection<E> edges = graph.getEdges();
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> numberOfSubgraphs4_2 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_2(edge)).sum()).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_3 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_3(edge)).sum() / 2).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_4 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_4(edge)).sum() / 4).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_5 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_5(edge)).sum() / 6).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_6 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_6(edge)).sum() / 12).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_1 = vertices.stream().parallel().mapToInt(vertex -> counter.getNumberOfSubgraphs4_1(vertex)).sum() - numberOfSubgraphs4_3 - 2 * numberOfSubgraphs4_5 - 4 * numberOfSubgraphs4_6).get();
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Andrey Kurchanov
	 */
	@Override
    public String toString() {
		int numberOfSubgraphs = numberOfSubgraphs4_1 + numberOfSubgraphs4_2 + numberOfSubgraphs4_3 + numberOfSubgraphs4_4 + numberOfSubgraphs4_5 + numberOfSubgraphs4_6;		
    	return String.format("Number of subgraphs4_1 = %d(%.3f%%). "
    						+"Number of subgraphs4_2 = %d(%.3f%%). "
    						+"Number of subgraphs4_3 = %d(%.3f%%). "
    						+"Number of subgraphs4_4 = %d(%.3f%%). "
    						+"Number of subgraphs4_5 = %d(%.3f%%). "
    						+"Number of subgraphs4_6 = %d(%.3f%%).",
    						numberOfSubgraphs4_1, (double)numberOfSubgraphs4_1 / numberOfSubgraphs * 100.0, 
    						numberOfSubgraphs4_2, (double)numberOfSubgraphs4_2 / numberOfSubgraphs * 100.0,
    						numberOfSubgraphs4_3, (double)numberOfSubgraphs4_3 / numberOfSubgraphs * 100.0,
    						numberOfSubgraphs4_4, (double)numberOfSubgraphs4_4 / numberOfSubgraphs * 100.0,
    						numberOfSubgraphs4_5, (double)numberOfSubgraphs4_5 / numberOfSubgraphs * 100.0,
    						numberOfSubgraphs4_6, (double)numberOfSubgraphs4_6 / numberOfSubgraphs * 100.0);
    }

}