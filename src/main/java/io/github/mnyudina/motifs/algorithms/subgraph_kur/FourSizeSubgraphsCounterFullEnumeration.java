package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;

/**
 * @author Andrey Kurchanov
 */
public class FourSizeSubgraphsCounterFullEnumeration<V, E> {

	private Graph<V, E> graph;
	
	/**
	 * Constructs and initializes the class.
	 *
	 * @author Andrey Kurchanov
	 * @param graph the graph
	 */
	public FourSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph) {
		this.graph = (Graph<V, E>) graph;
    }
	
	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_1 using the formula
	 * for calculating the number of 3-combinations from number of the
	 * successors of the <code>vertex</code> in the <code>graph</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param vertex of the graph
	 * @return number of subgraphs4_1, that are rooted by the <code>vertex</code>
	 */
	public int getNumberOfSubgraphs4_1(V vertex) {
		int numberOfSuccessors = graph.getSuccessors(vertex).size();
		int numberOfSubgraphs4_1 = (numberOfSuccessors * (numberOfSuccessors - 1) * (numberOfSuccessors - 2) / 6);
		return numberOfSubgraphs4_1;
	}
	
	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_2 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param edge of the graph
	 * @return number of subgraphs4_2, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_2(E edge) {
		int numberOfSubgraphs4_2 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_2++;
					}
				}
			}
		}
		return numberOfSubgraphs4_2;
	}
	
	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_3 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param edge of the graph
	 * @return number of subgraphs4_3, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_3(E edge) {
		int numberOfSubgraphs4_3 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if ((graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) ||
						(!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4))) {
						numberOfSubgraphs4_3++;
					}
				}
			}
		}
		return numberOfSubgraphs4_3;
	}
	
	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_4 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param edge of the graph
	 * @return number of subgraphs4_4, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_4(E edge) {
		int numberOfSubgraphs4_4 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_4++;
					}
				}
			}
		}
		return numberOfSubgraphs4_4;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_5 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param edge of the graph
	 * @return number of subgraphs4_5, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_5(E edge) {
		int numberOfSubgraphs4_5 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if ((graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) ||
					    (graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) ||
					    (!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4))) {
						numberOfSubgraphs4_5++;
					}
				}
			}
		}
		return numberOfSubgraphs4_5;
	}
	
	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_6 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
	 * @author Andrey Kurchanov
	 * @param edge of the graph
	 * @return number of subgraphs4_6, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_6(E edge) {
		int numberOfSubgraphs4_6 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_6++;
					}
				}
			}
		}
		return numberOfSubgraphs4_6;
	}
}