package io.github.mnyudina.motifs.graph;

import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.*;

/**
 * An implementation of <code>edu.uci.ics.jung.graph.Graph</code> which
 * represents the graph as an adjacency list and list of the edges. The
 * implementation permits directed or undirected edges only.
 *
 * @author Andrey Kurchanov
 */
public class AdjacencyListGraph<V,E> extends AbstractGraph<V,E> {

    private Map<V, List<V>> vertices;
    
    private Map<E, Pair<? extends V>> edges;

    private EdgeType edgeType;

    public AdjacencyListGraph() {
        vertices = new HashMap<>();
        edges = new HashMap<E, Pair<? extends V>>();
    }
    
    @Override
    public boolean addVertex(V vertex) {
    	if (!containsVertex(vertex)) {
            // Add the vertex with an empty list of outgoing edges.
    		vertices.put(vertex, new ArrayList<>());
    	}
        return true;
    }
    
    @Override
    public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType) {
        if (!containsVertex(endpoints.getFirst()) || !containsVertex(endpoints.getSecond())) {
        	return false;
        }
    	
    	this.edgeType = edgeType;

        if (!vertices.get(endpoints.getFirst()).contains(endpoints.getSecond())) {
        	// Add the edge.
        	if (edgeType == EdgeType.UNDIRECTED) {
        		vertices.get(endpoints.getFirst()).add(endpoints.getSecond());
        		vertices.get(endpoints.getSecond()).add(endpoints.getFirst());
        	} else {
        		vertices.get(endpoints.getFirst()).add(endpoints.getSecond());
        	}
        	edges.put(edge, endpoints);
        }
        return true;
    }

    @Override
    public Collection<V> getVertices() {
        return new ArrayList<>(vertices.keySet());
    }

    @Override
    public boolean containsVertex(V vertex) {
        return vertices.containsKey(vertex);
    }

    @Override
    public int getVertexCount() {
    	return vertices.size();
    }

    @Override
    public int getEdgeCount() {
    	return edges.size();
    }

    @Override
    public Collection<V> getNeighbors(V vertex) {
        Collection<V> neighbors = new ArrayList<>(getSuccessors(vertex));
        if (edgeType == EdgeType.DIRECTED) {
            neighbors.addAll(getPredecessors(vertex));
        }
        return neighbors;
    }

    @Override
    public EdgeType getEdgeType(E edge) {
        return edgeType;
    }

    @Override
    public EdgeType getDefaultEdgeType() {
        return edgeType;
    }

    @Override
    public int getEdgeCount(EdgeType edge_type) {
    	return edge_type == this.edgeType ? getEdgeCount() : 0;
    }

    @Override
    public Collection<V> getPredecessors(V vertex) {
    	Collection<V> predecessors = new ArrayList<>();
        for (Map.Entry<V, List<V>> pair : vertices.entrySet()) {
            if (pair.getValue().contains(vertex)) {
                predecessors.add(pair.getKey());
            }
        }
        return predecessors;
    }

    @Override
    public Collection<V> getSuccessors(V vertex) {
        return new ArrayList<>(vertices.get(vertex));
    }

    @Override
    public Collection<E> getEdges() {
        return new ArrayList<>(edges.keySet());
    }
    
    @Override
    public boolean containsEdge(E edge) {
        return edges.containsKey(edge);
    }
    
    @Override
    public boolean removeVertex(V vertex) {
        vertices.remove(vertex);
        return true;
    }
    
    @Override
    public boolean removeEdge(E edge) {
    	edges.remove(edge);
        return true;
    }
    
    @Override
    public Pair<V> getEndpoints(E edge) {
        return (Pair<V>) edges.get(edge);
    }
    
    @Override
    public boolean isNeighbor(V v1, V v2) {
    	return vertices.get(v1).contains(v2);
    }
    
    @Override
    public Collection<E> getIncidentEdges(V vertex) {
    	Collection<E> incidentEdges = new ArrayList<>(getOutEdges(vertex));
    	incidentEdges.addAll(getInEdges(vertex));
    	return incidentEdges;
    }
    
    @Override
    public Collection<E> getEdges(EdgeType edge_type) {
        return edge_type == this.edgeType ? new ArrayList<>(edges.keySet()) : new ArrayList<>();
    }
    
    @Override
    public Collection<E> getInEdges(V vertex) {
        List<E> inEdges = new ArrayList<>();
    	for (E edge : getEdges()) {
			if (getEndpoints(edge).getSecond() == vertex) {
				inEdges.add(edge);
			}
		}
    	return inEdges;
    }

    @Override
    public Collection<E> getOutEdges(V vertex) {
    	List<E> outEdges = new ArrayList<>();
    	for (E edge : getEdges()) {
			if (getEndpoints(edge).getFirst() == vertex) {
				outEdges.add(edge);
			}
		}
    	return outEdges;
    }
    
    @Override
    public V getSource(E directed_edge) {
        return edges.get(directed_edge).getFirst();
    }

    @Override
    public V getDest(E directed_edge) {
    	return edges.get(directed_edge).getSecond();
    }

    @Override
    public boolean isSource(V vertex, E edge) {
    	return edges.get(edge).getFirst() == vertex;
    }

    @Override
    public boolean isDest(V vertex, E edge) {
    	return edges.get(edge).getSecond() == vertex;
    }
    
}