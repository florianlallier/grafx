package fr.florianlallier.grafx.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Edge extends Group implements Comparable<Object> {

	/* Data */
	private Vertex source;
	private Vertex target;
	private int value;
	/* GUI */
	private Line line;
	private Text text;

	public Edge(Vertex source, Vertex target, int value) {
		/* Data */
		this.source = source;
		this.target = target;
		this.value = value;
		/* GUI */
		this.line = new Line();
		this.text = new Text(Integer.toString(value));
		this.text.setFont(Font.font(null, FontWeight.BOLD, 15));
		this.refreshPosition();
		this.getChildren().addAll(this.line, this.text);
	}

	public Vertex getSource() {
		return this.source;
	}

	public Vertex getTarget() {
		return this.target;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
		this.text.setText(Integer.toString(value));
	}
	
	public Line getLine() {
		return this.line;
	}
	
	public Text getText() {
		return this.text;
	}
	
	public String getName() {
		return this.getSource().toString() + this.getTarget().toString();
	}

	public void refreshPosition() {
		this.line.setStartX(this.source.getX());
		this.line.setStartY(this.source.getY());
		this.line.setEndX(this.target.getX());
		this.line.setEndY(this.target.getY());
		this.text.setX((this.source.getX() + this.target.getX()) / 2 - 5);
		this.text.setY((this.source.getY() + this.target.getY()) / 2 + 5);
	}

	public void select(Color color) {
		this.line.setStroke(color);
		this.text.setFill(color);
	}

	public boolean equals(String sourceName, String targetName) {
		return (this.source.equals(sourceName) && this.target.equals(targetName))
				|| (this.source.equals(targetName) && this.target.equals(sourceName));
	}

	@Override
	public String toString() {
		return this.getSource().toString() + this.getTarget().toString() + " (" + this.value + ")";
	}

	@Override
	public int compareTo(Object o) {
		Edge edge = (Edge) o;
		if (this.value < edge.value) {
			return -1;
		} else if (this.value > edge.value) {
			return 1;
		} else { // equality
			return 0;
		}
	}
}