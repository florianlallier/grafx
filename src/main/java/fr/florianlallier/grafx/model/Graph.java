package fr.florianlallier.grafx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import javafx.scene.paint.Color;

public class Graph {

	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	private ArrayList<Edge> edgesKruskal;

	public Graph() {
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
	}

	public ArrayList<Vertex> getVertices() {
		return this.vertices;
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public ArrayList<Edge> getEdgesKruskal() {
		this.edgesKruskal = new ArrayList<Edge>();
		return this.edgesKruskal;
	}

	public void addVertex(Vertex vertex) {
		this.vertices.add(vertex);
	}

	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}

	public void removeVertex(Vertex vertex) {
		this.vertices.remove(vertex);
	}

	public void removeEdge(Edge edge) {
		this.edges.remove(edge);
	}

	public boolean containsVertex(String name) {
		ListIterator<Vertex> iterator = this.vertices.listIterator();
		while (iterator.hasNext()) {
			Vertex vertex = iterator.next();
			if (vertex.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEdge(String sourceName, String targetName) {
		ListIterator<Edge> iterator = this.edges.listIterator();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			if (edge.equals(sourceName, targetName)) {
				return true;
			}
		}
		return false;
	}

	public Vertex getVertex(String name) {
		ListIterator<Vertex> iterator = this.vertices.listIterator();
		while (iterator.hasNext()) {
			Vertex vertex = iterator.next();
			if (vertex.equals(name)) {
				return vertex;
			}
		}
		return null;
	}

	public Edge getEdge(String sourceName, String targetName) {
		ListIterator<Edge> iterator = this.edges.listIterator();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			if (edge.equals(sourceName, targetName)) {
				return edge;
			}
		}
		return null;
	}

	public ArrayList<Edge> getEdges(Vertex vertex) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ListIterator<Edge> iterator = this.edges.listIterator();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			if (edge.getSource() == vertex || edge.getTarget() == vertex) {
				edges.add(edge);
			}
		}
		return edges;
	}

	public void clear() {
		this.edges.clear();
		this.vertices.clear();
	}

	public ArrayList<Edge> sortEdges() {
		Collections.sort(this.edges);
		return this.edges;
	}
}