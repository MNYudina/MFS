package io.github.mnyudina.motifs.algorithms.subgraph_myold;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import io.github.mnyudina.motifs.algorithms.Executor;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * full enumeration algorithm.
 * 
 * @author Yudin Evgeniy, Yudina M.
 */
public class MSF3Undir<V, E>  implements Executor {
	private boolean isParallel;

	private Graph<V, E> graph;


	private int numberOfForks, numberOfTriangles;

	/**
	 * Constructs and initializes the class.
	 *
	 * @author Yudina M.
	 * @param graph
	 *            the graph
	 */
	public MSF3Undir(Graph<V, E> graph, boolean isParallel) {
		this.graph = graph;
		this.isParallel =isParallel;

	}
	/**
	 * Calculates number of <code>vertex</code>'s "forks" using the formula for
	 * calculating the number of 2-combinations from number of the successors of
	 * the <code>vertex</code> in the <code>graph</code>.
	 *
	 * @author  Yudin Evgeniy, Yudina M.
	 * @param vertex
	 *            of the graph
	 * @return number of "forks", that are rooted by the <code>vertex</code>
	 */
	public int getNumberOfForks(V vertex) {
		int numberOfSuccessors = graph.getSuccessors(vertex).size();
		int numberOfForks = (numberOfSuccessors * (numberOfSuccessors - 1) / 2);
		return numberOfForks;
	}
	
	/**
	 * Calculates number of <code>vertex</code>'s "triangles" by testing
	 * connectivity of each pair of vertices from <code>vertex</code>'s
	 * successors list.
	 *
	 * @author  Yudin Evgeniy, Yudina M.
	 * @param vertex
	 *            of the graph
	 * @return number of "triangles", that are included the <code>vertex</code>
	 */
	public int getNumberOfTriangles(V vertex) {
		List<V> successors = new ArrayList<>(graph.getSuccessors(vertex));
		int numberOfTriangles = 0;
		for (int i = 0; i < successors.size(); i++) {
			for (int j = i + 1; j < successors.size(); j++) {
				if (graph.getSuccessors(successors.get(i)).contains(successors.get(j))) {
					numberOfTriangles++;
				}
			}
		}
		return numberOfTriangles;
	}
	
	/**
	 * Saves exact number of the <code>graph</code>'s "triangles" into
	 * <code>numberOfTriangles</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s "forks" into
	 * <code>numberOfForks</code> variable.<br>
	 * If the <code>graph</code> includes directed edges then
	 * <code>com.asoiu.simbigraph.exception.UnsupportedEdgeTypeException</code>
	 * is thrown.
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Yudin Evgeniy, Yudina M.
	 */
	
	public void execute()  {
		Collection<V> vertices = graph.getVertices();
		if(isParallel){
			numberOfTriangles = vertices.stream().parallel().mapToInt(vertex -> getNumberOfTriangles(vertex) ).sum()/3;
			numberOfForks = vertices.stream().parallel().mapToInt(vertex -> getNumberOfForks(vertex) ).sum()- numberOfTriangles * 3;

		}else{
			numberOfTriangles = vertices.stream().mapToInt(vertex -> getNumberOfTriangles(vertex) ).sum()/3;
			numberOfForks = vertices.stream().mapToInt(vertex -> getNumberOfForks(vertex) ).sum()- numberOfTriangles * 3;
		}

		
		/*ForkJoinPool forkJoinPool = new ForkJoinPool();
		
		try {
			forkJoinPool.submit(() -> numberOfTriangles = vertices.stream().parallel()
					.mapToInt(vertex -> getNumberOfTriangles(vertex)).sum() / 3).get();
			forkJoinPool.submit(() -> numberOfForks = vertices.stream().parallel()
					.mapToInt(vertex -> getNumberOfForks(vertex)).sum() - numberOfTriangles * 3).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
*/	}

	/**
	 * @author Yudina M.N.
	 */
	@Override
	public String toString() {
		String ret = ""+this.getClass().getSimpleName()+"\n";
		ret = ret+"V:"+graph.getVertexCount()+" E:"+ graph.getEdgeCount()+"\n";

		int numberOfSubgraphs = numberOfForks + numberOfTriangles;
		return String.format(ret+"Number of forks = %d(%.3f%%). Number of triangles = %d(%.3f%%).", numberOfForks,
				(double) numberOfForks / numberOfSubgraphs * 100.0, numberOfTriangles,
				(double) numberOfTriangles / numberOfSubgraphs * 100.0);
	}



}