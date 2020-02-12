package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.Hypergraph;

/**
 * @author Andrey Kurchanov
 */
public class ThreeSizeSubgraphsCounterFullEnumeration<V, E> {
	
	private Hypergraph<V, E> graph;
	
	/**
	 * Constructs and initializes the class.
	 *
	 * @author Andrey Kurchanov
	 * @param graph the graph
	 */
	public ThreeSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph) {
        this.graph = graph;
    }
	
	/**
	 * Calculates number of <code>vertex</code>'s "forks" using the formula for
	 * calculating the number of 2-combinations from number of the successors of
	 * the <code>vertex</code> in the <code>graph</code>.
	 *
	 * @author Andrey Kurchanov
	 * @param vertex of the graph
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
	 * @author Andrey Kurchanov
	 * @param vertex of the graph
	 * @return number of "triangles", that are included the <code>vertex</code>
	 */
	public int getNumberOfTriangles(V vertex) {
		List<V> successors = new ArrayList<>(graph.getSuccessors(vertex)); 
		int numberOfTriangles = 0;
		for (int i = 0; i < successors.size(); i++) {
			for (int j = i+1; j < successors.size(); j++) {
				if (graph.getSuccessors(successors.get(i)).contains(successors.get(j))) {
					numberOfTriangles++;
				}
			}
		}	
		return numberOfTriangles;
	}
	
}