package io.github.mnyudina.motifs.algorithms.subgraph_kur;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Hypergraph;

/**
 * @author Andrey Kurchanov
 */
public class ThreeSizeSubgraphsCounterSampling<V, E> {

	private Hypergraph<V, E> graph;
	
	private Map<Integer, ParallelThreeSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayers;
	
	/**
	 * Constructs and initializes the class.
	 *
	 * @author Andrey Kurchanov
	 * @param graph the graph
	 * @param vertexLayers layers of the vertices
	 */
	public ThreeSizeSubgraphsCounterSampling(Hypergraph<V, E> graph, Map<Integer, ParallelThreeSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayers) {
        this.graph = graph;
        this.vertexLayers = vertexLayers;
    }

	/**
	 * Chooses one of the <code>graph</code>'s "forks" randomly and tests if it
	 * is a <code>graph</code>'s "triangle".
	 * 
	 * @author Andrey Kurchanov
	 * @return 1 if "triangle" was explored or 0 if "fork" was explored
	 */
	public int doRun() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		ParallelThreeSizeSubgraphsCounterSampling.VertexLayerParameters<V> selectedVertexLayer = null;
		
		// Choose a layer of vertices taking into account the probabilities of layers selection
		for (Entry<Integer, ParallelThreeSizeSubgraphsCounterSampling.VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}
		
		// Choose a vertex from the layer of vertices randomly
		V selectedVertex = selectedVertexLayer.getVerticies().get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
		
		// Choose 2 successors of the vertex randomly
		List<V> selectedVertexSuccessorsList = new LinkedList<>(graph.getSuccessors(selectedVertex));
		int randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor1 = selectedVertexSuccessorsList.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor2 = selectedVertexSuccessorsList.remove(randomIntValue);
		
		if (graph.getSuccessors(successor1).contains(successor2)) {
			return 1;
		} else {
			return 0;
		}
	}
	
}