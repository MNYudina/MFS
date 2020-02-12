package io.github.mnyudina.motifs.graph;

import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.*;

/**
 * An implementation of <code>edu.uci.ics.jung.graph.Graph</code> which
 * represents the graph as an adjacency list and list of the edges. The
 * implementation permits directed or undirected edges only.
 *
 * @author Gleepa
 */
public class MySparseGraph<V,E>  extends SparseGraph<V,E> {
    @Override
    public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType)
    {
        Pair<V> new_endpoints = getValidatedEndpoints(edge, endpoints);
        if (new_endpoints == null)
            return false;

        V v1 = new_endpoints.getFirst();
        V v2 = new_endpoints.getSecond();

        // undirected edges and directed edges are not considered to be parallel to each other,
        // so as long as anything that's returned by findEdge is not of the same type as
        // edge, we're fine
        E connection = findEdge(v1, v2);
        if (connection != null && getEdgeType(connection) == edgeType)
            return false;

        if (!containsVertex(v1))
            this.addVertex(v1);

        if (!containsVertex(v2))
            this.addVertex(v2);

        // map v1 to <v2, edge> and vice versa
        if (edgeType == EdgeType.DIRECTED)
        {
            // Нужно изменить здесь, если противоположная дуга есть, то добавить ребро, а не дугу
            // а противоположную дугу удалить
            E retEdge =findEdge(v2, v1);
            if(retEdge==null) {
                vertex_maps.get(v1)[OUTGOING].put(v2, edge);
                vertex_maps.get(v2)[INCOMING].put(v1, edge);
                directed_edges.put(edge, new_endpoints);
            }
            else{
                removeEdge(retEdge);
                vertex_maps.get(v1)[INCIDENT].put(v2, edge);
                vertex_maps.get(v2)[INCIDENT].put(v1, edge);
                // хотелось бы,чтобы с этим new_endpoints в undirected_edges было пусто
                undirected_edges.put(edge, new_endpoints);
            }
        }
        else
        {
            vertex_maps.get(v1)[INCIDENT].put(v2, edge);
            vertex_maps.get(v2)[INCIDENT].put(v1, edge);
            undirected_edges.put(edge, new_endpoints);
        }

        return true;
    }

    public List<V> getNeighbors(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        // consider directed edges and undirected edges
        List<V> neighbors = new ArrayList<V>(vertex_maps.get(vertex)[INCOMING].keySet());
        neighbors.addAll(vertex_maps.get(vertex)[OUTGOING].keySet());
        neighbors.addAll(vertex_maps.get(vertex)[INCIDENT].keySet());
        return Collections.unmodifiableList(neighbors);
    }
    public EdgeType getDefaultEdgeType()
    {
        return EdgeType.DIRECTED;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Vertices:"+getVertexCount());
        sb.setLength(sb.length()-1);
        sb.append("\nEdges:" +undirected_edges.keySet().size());
        sb.append("\nArcs:"+directed_edges.keySet().size());
        return sb.toString();
    }

    public List<E> getIncidentEdges(V vertex)
    {
        if (!containsVertex(vertex))
            return null;
        List<E> incident = new ArrayList<E>(vertex_maps.get(vertex)[INCOMING].values());
        incident.addAll(vertex_maps.get(vertex)[OUTGOING].values());
        incident.addAll(vertex_maps.get(vertex)[INCIDENT].values());
        return Collections.unmodifiableList(incident);
    }

}