package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;

/**
 * @author Andrey Kurchanov
 */
public class FourSizeSubgraphsCounterSampling<V, E> {
	
	private Graph<V, E> graph;
	
	private Map<Integer, ParallelFourSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayers;

	private Map<Integer, ParallelFourSizeSubgraphsCounterSampling.EdgeLayerParameters<E>> edgeLayers;

	/**
	 * Constructs and initializes the class.
	 *
	 * @author Andrey Kurchanov
	 * @param graph the graph
	 * @param vertexLayers layers of the vertices
	 * @param edgeLayers layers of the edges
	 */
	public FourSizeSubgraphsCounterSampling(Hypergraph<V, E> graph, Map<Integer, ParallelFourSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayers, Map<Integer, ParallelFourSizeSubgraphsCounterSampling.EdgeLayerParameters<E>> edgeLayers) {
        this.graph = (Graph<V, E>) graph;
        this.vertexLayers = vertexLayers;
        this.edgeLayers = edgeLayers;
    }

	/**
	 * Chooses one of the <code>graph</code>'s vertices and 3 successors
	 * randomly.<br>
	 * Then tests if it is a <code>graph</code>'s subgraph4_1.
	 * 
	 * @author Andrey Kurchanov
	 * @return 1 if subgraph4_1 was explored or 0 if wasn't
	 */
	public int searchSubgraphs4_1() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		ParallelFourSizeSubgraphsCounterSampling.VertexLayerParameters<V> selectedVertexLayer = new ParallelFourSizeSubgraphsCounterSampling.VertexLayerParameters<>();
				
		// Choose a layer of vertices taking into account the probabilities of layers selection
		for (Entry<Integer, ParallelFourSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}
		
		V selectedVertex = selectedVertexLayer.getVerticies().get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
		
		// Choose 3 successors of the vertex randomly
		List<V> selectedVertexSuccessorsList = new LinkedList<>(graph.getSuccessors(selectedVertex));
		int randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor1 = selectedVertexSuccessorsList.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor2 = selectedVertexSuccessorsList.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor3 = selectedVertexSuccessorsList.remove(randomIntValue);
		
		if (!graph.getSuccessors(successor1).contains(successor2) &&
			!graph.getSuccessors(successor1).contains(successor3) &&
			!graph.getSuccessors(successor2).contains(successor3)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Chooses one of the <code>graph</code>'s path of length three randomly and
	 * determines which type of subgraph it is.
	 * 
	 * @author Andrey Kurchanov
	 * @return 6 if subgraph4_6 was explored,
	 * 		   5 if subgraph4_5 was explored,
	 * 		   4 if subgraph4_4 was explored,
	 * 		   3 if subgraph4_3 was explored,
	 * 		   2 if subgraph4_2 was explored,
	 * 		   0 if selected <code>graph</code>'s path of length three is a
	 * 		   <code>graph</code>'s "triangle"
	 */
	public int searchOtherTypesOfSubgraphs() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		ParallelFourSizeSubgraphsCounterSampling.EdgeLayerParameters<E> selectedEdgeLayer = null;
		
		// Choose a layer of edges taking into account the probabilities of layers selection
		for (Entry<Integer, ParallelFourSizeSubgraphsCounterSampling.EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			borderOfProbability += edgeLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedEdgeLayer = edgeLayer.getValue();
				break;
			}
		}
		
		// Choose an edge from the layer of edges randomly
		E selectedEdge = selectedEdgeLayer.getEdges().get(randomGenerator.nextInt(selectedEdgeLayer.getEdges().size()));
		
		// Get endpoints of the edge
		V v1 = graph.getEndpoints(selectedEdge).getFirst();
		V v2 = graph.getEndpoints(selectedEdge).getSecond();
		
		// Generate a list of successors of the endpoints
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);
		
		// Choose 2 successors of the endpoints randomly
		V v3 = successors1.get(randomGenerator.nextInt(successors1.size()));
		V v4 = successors2.get(randomGenerator.nextInt(successors2.size()));
		
		if (v3 == v4) {
			return 0;
		}
		
		if (graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
			return 6;
		}
		
		if ((graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) ||
			(graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) ||
			(!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4))) {
			return 5;
		}
		
		if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
			return 4;
		}
		
		if ((graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) ||
			(!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4))) {
			return 3;
		}
		
		if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) {
			return 2;
		}
		return 0;
	}
	
}