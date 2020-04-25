package fr.florianlallier.grafx.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import fr.florianlallier.grafx.Grafx;
import fr.florianlallier.grafx.model.Edge;
import fr.florianlallier.grafx.model.Graph;
import fr.florianlallier.grafx.model.Vertex;
import fr.florianlallier.grafx.view.View;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Controller {

	private Graph graph;

	public Controller() {
		this.graph = new Graph();
	}

	public Vertex addVertex(String shape, Color color, String name) {
		Vertex vertex = null;
		if (!this.graph.containsVertex(name)) {
			vertex = new Vertex(25, 25, shape, color, name);
			Grafx.LOGGER.info("Adding vertex " + vertex.toString());
			this.graph.addVertex(vertex);
		}
		return vertex;
	}

	public Vertex addVertexClick(double x, double y, String shape, Color color, String name) {
		Vertex vertex = null;
		if (!this.graph.containsVertex(name)) {
			vertex = new Vertex(x, y, shape, color, name);
			Grafx.LOGGER.info("Adding vertex " + vertex.toString());
			this.graph.addVertex(vertex);
		}
		return vertex;
	}

	public Edge addEdge(String sourceName, String targetName, String value) {
		Edge edge = null;
		if (this.graph.containsVertex(sourceName) && this.graph.containsVertex(targetName)
				&& !this.graph.containsEdge(sourceName, targetName)) { // create
			edge = new Edge(this.graph.getVertex(sourceName), this.graph.getVertex(targetName), Integer.valueOf(value));
			Grafx.LOGGER.info("Adding edge " + edge.toString());
			this.graph.addEdge(edge);
		} else if (this.graph.containsVertex(sourceName) && this.graph.containsVertex(targetName)
				&& this.graph.containsEdge(sourceName, targetName)) { // replace
			edge = this.graph.getEdge(sourceName, targetName);
			edge.setValue(Integer.valueOf(value));
			Grafx.LOGGER.info("Replacing edge " + edge.toString());
		}
		return edge;
	}

	public ArrayList<Edge> removeVertex(Vertex vertex) {
		ArrayList<Edge> edges = this.graph.getEdges(vertex);
		ListIterator<Edge> iterator = edges.listIterator();
		while (iterator.hasNext()) {
			this.removeEdge(iterator.next());
		}
		Grafx.LOGGER.info("Removing vertex " + vertex.toString());
		this.graph.removeVertex(vertex);
		return edges;
	}

	public void removeEdge(Edge edge) {
		Grafx.LOGGER.info("Removing edge " + edge.toString());
		this.graph.removeEdge(edge);
	}

	public void clear() {
		Grafx.LOGGER.info("Removing all vertices and all edges");
		this.graph.clear();
	}

	public void moveEdges(Vertex vertex) {
		ArrayList<Edge> edges = this.graph.getEdges(vertex);
		ListIterator<Edge> iterator = edges.listIterator();
		while (iterator.hasNext()) {
			iterator.next().refreshPosition();
		}
	}

	public void selectVertex(Vertex vertex, Color color) {
		ArrayList<Edge> edges = this.graph.getEdges(vertex);
		ListIterator<Edge> iterator = edges.listIterator();
		while (iterator.hasNext()) {
			iterator.next().select(color);
		}
		vertex.select(color);
	}

	public void selectEdge(Edge edge, Color color) {
		edge.getSource().select(color);
		edge.getTarget().select(color);
		edge.select(color);
	}

	public ArrayList<Node> open(File file) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(file);
			Element root = document.getRootElement();
			/* Vertices */
			List<Element> vertices = root.getChildren("vertex");
			Iterator<Element> iteratorVertices = vertices.iterator();
			while (iteratorVertices.hasNext()) {
				Element elementVertex = iteratorVertices.next();
				double x = Double.valueOf(elementVertex.getChild("x").getText());
				double y = Double.valueOf(elementVertex.getChild("y").getText());
				String shape = elementVertex.getChild("shape").getText();
				Color color = Color.valueOf(elementVertex.getChild("color").getText());
				String name = elementVertex.getChild("name").getText();
				Vertex vertex = new Vertex(x, y, shape, color, name);
				Grafx.LOGGER.info("Adding vertex " + vertex.toString());
				this.graph.addVertex(vertex);
				nodes.add(vertex);
			}
			/* Edges */
			List<Element> edges = root.getChildren("edge");
			Iterator<Element> iteratorEdges = edges.iterator();
			while (iteratorEdges.hasNext()) {
				Element elementEdge = iteratorEdges.next();
				String sourceName = elementEdge.getChild("source_name").getText();
				String targetName = elementEdge.getChild("target_name").getText();
				String value = elementEdge.getChild("value").getText();
				Edge edge = new Edge(this.graph.getVertex(sourceName), this.graph.getVertex(targetName),
						Integer.valueOf(value));
				Grafx.LOGGER.info("Adding edge " + edge.toString());
				this.graph.addEdge(edge);
				nodes.add(edge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodes;
	}

	public void saveAs(File file) {
		try {
			Element root = new Element("graph");
			Document document = new Document(root);
			/* Vertices */
			ArrayList<Vertex> vertices = this.graph.getVertices();
			Iterator<Vertex> iteratorVertices = vertices.iterator();
			while (iteratorVertices.hasNext()) {
				Vertex vertex = iteratorVertices.next();
				Element elementVertex = new Element("vertex");
				root.addContent(elementVertex);
				/* X */
				Element x = new Element("x");
				x.setText(String.valueOf(vertex.getX()));
				elementVertex.addContent(x);
				/* Y */
				Element y = new Element("y");
				y.setText(String.valueOf(vertex.getY()));
				elementVertex.addContent(y);
				/* Shape */
				Element shape = new Element("shape");
				shape.setText(vertex.getShape());
				elementVertex.addContent(shape);
				/* Color */
				Element color = new Element("color");
				color.setText(String.valueOf(vertex.getColor()));
				elementVertex.addContent(color);
				/* Name */
				Element name = new Element("name");
				name.setText(vertex.getName());
				elementVertex.addContent(name);
			}
			/* Edges */
			ArrayList<Edge> edges = this.graph.getEdges();
			Iterator<Edge> iteratorEdges = edges.iterator();
			while (iteratorEdges.hasNext()) {
				Edge edge = iteratorEdges.next();
				Element elementEdge = new Element("edge");
				root.addContent(elementEdge);
				/* Source name */
				Element sourceName = new Element("source_name");
				sourceName.setText(edge.getSource().getName());
				elementEdge.addContent(sourceName);
				/* Target name */
				Element targetName = new Element("target_name");
				targetName.setText(edge.getTarget().getName());
				elementEdge.addContent(targetName);
				/* Value */
				Element value = new Element("value");
				value.setText(String.valueOf(edge.getValue()));
				elementEdge.addContent(value);
			}
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(document, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Timeline> start(Text step1, Text[] logs, Text note1, Text step2, Text[] logs2, Text note2, Text weight) {
		ArrayList<Timeline> timelines = new ArrayList<Timeline>();

		ArrayList<Vertex> vertices = this.graph.getVertices();
		ArrayList<Edge> edges = this.graph.sortEdges();
		ArrayList<Edge> edgesKruskal = this.graph.getEdgesKruskal();
		int nbVertices = vertices.size();

		for (int i = 0; i < nbVertices; i++) {
			vertices.get(i).setMark(i);
		}

		Timeline timeline1 = new Timeline();
		timeline1.getKeyFrames()
				.add(new KeyFrame(Duration.ONE, new KeyValue(step1.textProperty(), "\nStep 1: sort the edges\n")));
		timelines.add(timeline1);

		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			Timeline timeline2 = new Timeline();
			KeyValue value1 = new KeyValue(logs[i].textProperty(), "• " + edge.toString());
			KeyValue value2 = new KeyValue(edge.getLine().strokeProperty(), Color.RED);
			KeyValue value3 = new KeyValue(edge.getText().fillProperty(), Color.RED);
			KeyValue value4 = new KeyValue(edge.getLine().strokeProperty(), View.DEFAULT_COLOR);
			KeyValue value5 = new KeyValue(edge.getText().fillProperty(), View.DEFAULT_COLOR);
			KeyFrame frame1 = new KeyFrame(Duration.millis(2000), value1, value2, value3);
			KeyFrame frame2 = new KeyFrame(Duration.millis(4000), value4, value5);
			timeline2.getKeyFrames().addAll(frame1, frame2);
			timelines.add(timeline2);
		}

		Timeline timeline3 = new Timeline();
		timeline3.getKeyFrames()
				.add(new KeyFrame(Duration.ONE, new KeyValue(note1.textProperty(), "\nNote: Our graph has " + nbVertices
						+ " vertices so, our MST will have " + (nbVertices - 1) + " edges.")));
		timelines.add(timeline3);

		Timeline timeline4 = new Timeline();
		timeline4.getKeyFrames()
				.add(new KeyFrame(Duration.ONE, new KeyValue(step2.textProperty(), "\nStep 2: find the MST\n")));
		timelines.add(timeline4);

		int result = 0;
		int i = 0;
		while (edgesKruskal.size() < nbVertices - 1) {
			Edge edge = edges.get(i);
			Timeline timeline5 = new Timeline();
			int sourceMark = edge.getSource().getMark();
			int targetMark = edge.getTarget().getMark();
			if (sourceMark == targetMark) { // circuit
				KeyValue value1 = new KeyValue(logs2[i].textProperty(),
						"• " + edge.getValue() + " is the smallest weight so we will select edge " + edge.getName()
								+ ",\n but if we select edge " + edge.getName() + " then it will form a circuit.");
				KeyValue value2 = new KeyValue(edge.getLine().strokeProperty(), Color.RED);
				KeyValue value3 = new KeyValue(edge.getText().fillProperty(), Color.RED);
				timeline5.getKeyFrames().add(new KeyFrame(Duration.millis(2000), value1, value2, value3));
			} else {
				result += edge.getValue();
				edgesKruskal.add(edge);
				for (Vertex vertex : vertices) {
					if (vertex.getMark() == targetMark) {
						vertex.setMark(sourceMark);
					}
				}

				KeyValue value1 = new KeyValue(logs2[i].textProperty(), "• " + edge.getValue()
						+ " is the smallest weight so we will select edge " + edge.getName() + ".");
				KeyValue value2 = new KeyValue(edge.getLine().strokeProperty(), Color.GREEN);
				KeyValue value3 = new KeyValue(edge.getText().fillProperty(), Color.GREEN);
				KeyValue value4 = new KeyValue(edge.getSource().test().strokeProperty(), Color.GREEN);
				KeyValue value5 = new KeyValue(edge.getTarget().test().strokeProperty(), Color.GREEN);
				timeline5.getKeyFrames()
						.add(new KeyFrame(Duration.millis(5000), value1, value2, value3, value4, value5));
			}
			timelines.add(timeline5);
			i++;
		}

		Timeline timeline6 = new Timeline();
		timeline6.getKeyFrames().add(new KeyFrame(Duration.ONE, new KeyValue(note2.textProperty(),
				"\nNote: Since we have got the " + (nbVertices - 1) + " edges so we will stop here.")));
		timelines.add(timeline6);

		Timeline timeline7 = new Timeline();
		timeline7.getKeyFrames().add(new KeyFrame(Duration.ONE, new KeyValue(weight.textProperty(),
				"\nThe weight of MST is " + result + ".")));
		timelines.add(timeline7);

		return timelines;
	}

	public int nbEdges() {
		return this.graph.getEdges().size();
	}
}