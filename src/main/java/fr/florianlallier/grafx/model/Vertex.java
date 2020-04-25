package fr.florianlallier.grafx.model;

import fr.florianlallier.grafx.view.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Vertex extends Group {
	
	/* Data */
	private String name;
	/* GUI */
	private double initialX;
	private double initialY;
	private Color color;
	private Shape shape;
	private Text text;
	/* Kruskal */
	private int mark;

	public Vertex(double x, double y, String shape, Color color, String name) {
		/* Data */
		this.name = name;
		/* GUI */
		this.initialX = x;
		this.initialY = y;
		this.color = color;
		if (shape.equals(View.CIRCLE)) {
			this.shape = new Circle(x, y, 20);
		} else {
			this.shape = new Rectangle(x - 20, y - 20, 40, 40);
		}
		this.shape.setFill(color);
		this.shape.setStroke(View.DEFAULT_COLOR);
		this.shape.setStrokeWidth(2);
		this.text = new Text(x - 5, y + 5, name);
		this.getChildren().addAll(this.shape, this.text);
	}
	
	public Shape test() {
		return this.shape;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getShape() {
		if (this.shape instanceof Circle) {
			return "Circle";
		} else {
			return "Rectangle";
		}
	}
	
	public double getX() {
		return this.initialX + this.getTranslateX();
	}
	
	public double getY() {
		return this.initialY + this.getTranslateY();
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getMark() {
		return this.mark;
	}
	
	public void setMark(int mark) {
		this.mark = mark;
	}

	public void select(Color color) {
		this.shape.setStroke(color);
	}

	public boolean equals(String name) {
		return name.equals(this.name);
	}

	@Override
	public String toString() {
		return this.name;
	}
}