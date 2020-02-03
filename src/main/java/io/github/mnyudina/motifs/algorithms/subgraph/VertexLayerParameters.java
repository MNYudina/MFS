package io.github.mnyudina.motifs.algorithms.subgraph;

import java.util.ArrayList;
import java.util.List;

public class VertexLayerParameters<V> {
	public double probability;
	
	public List<V> vertices = new ArrayList<>();
	
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
