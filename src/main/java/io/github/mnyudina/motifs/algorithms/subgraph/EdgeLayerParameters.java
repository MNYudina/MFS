package io.github.mnyudina.motifs.algorithms.subgraph;

import java.util.ArrayList;
import java.util.List;

public class EdgeLayerParameters <E>{
	
	public double probability;
	
	public List<E> edges = new ArrayList<>();
	
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
