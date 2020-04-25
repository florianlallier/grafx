package fr.florianlallier.grafx.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import fr.florianlallier.grafx.Grafx;
import fr.florianlallier.grafx.controller.Controller;
import fr.florianlallier.grafx.model.Edge;
import fr.florianlallier.grafx.model.Vertex;
import fr.florianlallier.grafx.util.Util;

import javafx.animation.SequentialTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class View extends BorderPane {

	public final static String ADDITION = "Addition";
	public final static String EDITION = "Édition";
	public final static String SUPPRESSION = "Suppression";
	public final static String CIRCLE = "Circle";
	public final static String SQUARE = "Square";
	public final static Color DEFAULT_COLOR = Color.BLACK;
	public final static Color SELECT_COLOR = Color.BLUE;

	private Controller controller;

	private MenuItem new_;
	private MenuItem open;
	private MenuItem saveAs;
	private MenuItem quit;
	private Pane pane;
	private ComboBox<String> mode;
	private ComboBox<String> vertexShape;
	private ColorPicker vertexColor;
	private TextField vertexName;
	private Button addVertex;
	private TextField edgeSourceVertexName;
	private TextField edgeTargetVertexName;
	private TextField edgeValue;
	private Button addEdge;
	private VBox box;
	private Button play;
	private Button pause;
	private Button stop;
	private Text step1;
	private Text[] logs1;
	private Text note1;
	private Text step2;
	private Text[] logs2;
	private Text note2;
	private Text weight;
	private EventHandler<MouseEvent> consumeFilter;
	private EventHandler<MouseEvent> pressedFilterAddition;
	private EventHandler<MouseEvent> pressedFilterEdition;
	private EventHandler<KeyEvent> rotateFilter;
	private Vertex pickedVertex;
	private SequentialTransition transitions;
	private boolean start;

	public View() {
		super();
		this.controller = new Controller();
		this.start = false;
		this.addPane();
		this.addMenuBar();
		this.addToolbar();
		this.addAlgorithmBar();
		this.addFilters();
		this.addHandlers();
		this.addListeners();
	}

	public void addPane() {
		this.pane = new Pane();
		this.pane.setStyle("-fx-background-color: white;");
		this.setCenter(this.pane);
	}

	public void addMenuBar() {
		MenuBar bar = new MenuBar();
		Menu file = new Menu("File");
		this.new_ = new MenuItem("New");
		this.open = new MenuItem("Open...");
		this.saveAs = new MenuItem("Save As...");
		this.quit = new MenuItem("Quit");

		/* Accelerators */
		this.new_.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN)); // Ctrl+N
		this.open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN)); // Ctrl+O
		this.saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN)); // Ctrl+S
		this.quit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN)); // Ctrl+Q

		file.getItems().addAll(this.new_, this.open, new SeparatorMenuItem(), this.saveAs, new SeparatorMenuItem(),
				this.quit);
		bar.getMenus().add(file);
		this.setTop(bar);
	}

	public void addToolbar() {
		ToolBar bar = new ToolBar();
		Label mode = new Label("Mode:");
		ObservableList<String> modes = FXCollections.observableArrayList(ADDITION, EDITION, SUPPRESSION);
		this.mode = new ComboBox<String>(modes);
		Separator separator1 = new Separator();
		separator1.setPadding(new Insets(0, 10, 0, 10));
		Label vertex = new Label("Vertex:");
		ObservableList<String> shapes = FXCollections.observableArrayList(CIRCLE, SQUARE);
		this.vertexShape = new ComboBox<String>(shapes);
		this.vertexShape.setValue(CIRCLE);
		this.vertexColor = new ColorPicker();
		this.vertexColor.setPrefWidth(50);
		this.vertexName = new TextField();
		this.vertexName.setPrefWidth(30);
		this.addVertex = new Button("Add vertex");
		Separator separator2 = new Separator();
		separator2.setPadding(new Insets(0, 10, 0, 10));
		Label edge = new Label("Edge:");
		this.edgeSourceVertexName = new TextField();
		this.edgeSourceVertexName.setPrefWidth(30);
		Label labelTo = new Label("to");
		this.edgeTargetVertexName = new TextField();
		this.edgeTargetVertexName.setPrefWidth(30);
		this.edgeValue = new TextField();
		this.edgeValue.setPrefWidth(50);
		this.addEdge = new Button("Add edge");
		bar.getItems().addAll(mode, this.mode, separator1, vertex, this.vertexShape, this.vertexColor, this.vertexName,
				this.addVertex, separator2, edge, this.edgeSourceVertexName, labelTo, this.edgeTargetVertexName,
				this.edgeValue, this.addEdge);
		this.setBottom(bar);
	}

	public void addAlgorithmBar() {
		this.box = new VBox();
		this.box.setPrefWidth(450);
		this.box.setPadding(new Insets(10, 10, 10, 10));
		Text text = new Text("Kruskal's algorithm:\n");
		text.setFont(Font.font(null, FontWeight.BOLD, 15));
		HBox hbox = new HBox(20);
		this.play = new Button("PLAY");
		this.pause = new Button("PAUSE");
		this.pause.setDisable(true);
		this.stop = new Button("STOP");
		this.stop.setDisable(true);
		hbox.getChildren().addAll(this.play, this.pause, this.stop);
		this.box.getChildren().addAll(text, hbox);
		this.setRight(this.box);
	}

	public void addFilters() {
		this.consumeFilter = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				event.consume();
			}
		};

		this.pressedFilterAddition = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String name = vertexName.getText().toUpperCase();
				if (!Util.isLetter(name)) {
					errorVertex(1);
					return;
				}
				Vertex vertex = controller.addVertexClick(event.getX(), event.getY(), vertexShape.getValue(),
						vertexColor.getValue(), name);
				if (vertex != null) {
					makeEditable(vertex);
					pane.getChildren().add(vertex);
				} else {
					errorVertex(2);
				}
			}
		};

		this.pressedFilterEdition = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pickedVertex = null;
				ObservableList<Node> nodes = pane.getChildren();
				Iterator<Node> iterator = nodes.iterator();
				while (iterator.hasNext()) {
					Node node = iterator.next();
					if (node instanceof Vertex) {
						controller.selectVertex((Vertex) node, DEFAULT_COLOR);
					} else {
						controller.selectEdge((Edge) node, DEFAULT_COLOR);
					}
				}
			}
		};

		this.rotateFilter = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (pickedVertex != null && !pickedVertex.getTransforms().isEmpty()) {
					double delta = 5;
					KeyCode code = event.getCode();
					Rotate rotate = (Rotate) pickedVertex.getTransforms().get(0);
					if (code == KeyCode.F1) {
						rotate.setAngle(rotate.getAngle() - delta);
					} else if (code == KeyCode.F2) {
						rotate.setAngle(rotate.getAngle() + delta);
					}
					Bounds bounds = pickedVertex.getBoundsInLocal();
					double x = (bounds.getMaxX() + bounds.getMinX()) / 2;
					double y = (bounds.getMaxY() + bounds.getMinY()) / 2;
					rotate.setPivotX(x);
					rotate.setPivotY(y);
				}
			}
		};
	}

	public void addHandlers() {
		this.new_.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.clear();
				pane.getChildren().clear();
			}
		});

		this.open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = open();
				if (file == null) { // closing file chooser
					return;
				} else if (Util.isXMLFile(file)) {
					controller.clear();
					pane.getChildren().clear();
					ArrayList<Node> nodes = controller.open(file);
					Iterator<Node> iterator = nodes.iterator();
					while (iterator.hasNext()) {
						Node node = iterator.next();
						if (node instanceof Vertex) {
							makeEditable((Vertex) node);
							pane.getChildren().add(node);
						} else {
							makeEditable((Edge) node);
							pane.getChildren().add(node);
							node.toBack(); // moves edge to the back of its
											// vertices
						}
					}
				} else if (!Util.isXMLFile(file)) {
					errorFile();
				}
			}
		});

		this.saveAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				File file = saveAs();
				if (file == null) { // closing file chooser
					return;
				} else if (Util.isXMLFile(file)) {
					controller.saveAs(file);
				} else if (!Util.isXMLFile(file)) {
					errorFile();
				}
			}
		});

		this.quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		this.addVertex.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = vertexName.getText().toUpperCase();
				if (!Util.isLetter(name)) {
					errorVertex(1);
					return;
				}
				Vertex vertex = controller.addVertex(vertexShape.getValue(), vertexColor.getValue(), name);
				if (vertex != null) {
					makeEditable(vertex);
					pane.getChildren().add(vertex);
				} else {
					errorVertex(2);
				}
			}
		});

		this.addEdge.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String sourceName = edgeSourceVertexName.getText().toUpperCase();
				String targetName = edgeTargetVertexName.getText().toUpperCase();
				String value = edgeValue.getText();
				if (!Util.isLetter(sourceName) || !Util.isLetter(targetName) || !Util.isNumber(value)) {
					errorEdge(1);
					return;
				}
				if (sourceName.equals(targetName)) {
					errorEdge(2);
					return;
				}
				Edge edge = controller.addEdge(sourceName, targetName, value);
				if (edge != null) {
					if (!pane.getChildren().contains(edge)) { // create
						makeEditable(edge);
					} else { // replace
						pane.getChildren().remove(edge);
					}
					pane.getChildren().add(edge);
					edge.toBack(); // moves edge to the back of its vertices
				} else {
					errorEdge(3);
				}
			}
		});

		this.play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (start) {
					transitions.play();
					play.setDisable(true);
					pause.setDisable(false);
					stop.setDisable(false);
				} else {
					int nbEdges = controller.nbEdges();

					step1 = new Text();
					step1.setFont(Font.font(null, FontWeight.BOLD, 12));
					box.getChildren().add(step1);

					logs1 = new Text[nbEdges];
					for (int i = 0; i < nbEdges; i++) {
						logs1[i] = new Text();
						box.getChildren().add(logs1[i]);
					}

					note1 = new Text();
					note1.setFont(Font.font(null, FontWeight.BOLD, 12));
					box.getChildren().add(note1);

					step2 = new Text();
					step2.setFont(Font.font(null, FontWeight.BOLD, 12));
					box.getChildren().add(step2);

					logs2 = new Text[nbEdges];
					for (int i = 0; i < nbEdges; i++) {
						logs2[i] = new Text();
						box.getChildren().add(logs2[i]);
					}

					note2 = new Text();
					note2.setFont(Font.font(null, FontWeight.BOLD, 12));
					box.getChildren().add(note2);
					
					weight = new Text();
					weight.setFont(Font.font(null, FontWeight.BOLD, 14));
					box.getChildren().add(weight);

					transitions = new SequentialTransition();
					try { // cheat of the death n° 1
						transitions.getChildren().addAll(controller.start(step1, logs1, note1, step2, logs2, note2, weight));
					} catch (Exception e) {
						errorAlgorithm();
					}
					transitions.play();
					start = true;
					play.setDisable(true);
					pause.setDisable(false);
					stop.setDisable(false);
					mode.setDisable(true);
					vertexShape.setDisable(true);
					vertexColor.setDisable(true);
					vertexName.setDisable(true);
					addVertex.setDisable(true);
					edgeSourceVertexName.setDisable(true);
					edgeTargetVertexName.setDisable(true);
					edgeValue.setDisable(true);
					addEdge.setDisable(true);
					new_.setDisable(true);
					open.setDisable(true);
					saveAs.setDisable(true);
					mode.setValue(mode.getValue() + " "); // cheat of the death n° 2
				}
			}
		});

		this.pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				transitions.pause();
				play.setDisable(false);
				pause.setDisable(true);
				stop.setDisable(false);
			}
		});

		this.stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				transitions.stop();
				start = false;
				mode.setDisable(false);
				vertexShape.setDisable(false);
				vertexColor.setDisable(false);
				vertexName.setDisable(false);
				addVertex.setDisable(false);
				edgeSourceVertexName.setDisable(false);
				edgeTargetVertexName.setDisable(false);
				edgeValue.setDisable(false);
				addEdge.setDisable(false);
				new_.setDisable(false);
				open.setDisable(false);
				saveAs.setDisable(false);
				mode.setValue(EDITION);
				addAlgorithmBar();
				addHandlers();
				pickedVertex = null;
				ObservableList<Node> nodes = pane.getChildren();
				Iterator<Node> iterator = nodes.iterator();
				while (iterator.hasNext()) {
					Node node = iterator.next();
					if (node instanceof Vertex) {
						controller.selectVertex((Vertex) node, DEFAULT_COLOR);
					} else {
						controller.selectEdge((Edge) node, DEFAULT_COLOR);
					}
				}
			}
		});
	}

	public void addListeners() {
		this.mode.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (mode.getValue().equals(ADDITION)) {
					pane.addEventFilter(MouseEvent.ANY, consumeFilter);
					pane.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterEdition);
					pane.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterAddition);
					pane.removeEventFilter(KeyEvent.KEY_PRESSED, rotateFilter);
					pickedVertex = null;
					ObservableList<Node> nodes = pane.getChildren();
					Iterator<Node> iterator = nodes.iterator();
					while (iterator.hasNext()) {
						Node node = iterator.next();
						if (node instanceof Vertex) {
							controller.selectVertex((Vertex) node, DEFAULT_COLOR);
						} else {
							controller.selectEdge((Edge) node, DEFAULT_COLOR);
						}
					}
				} else if (mode.getValue().equals(EDITION)) {
					pane.removeEventFilter(MouseEvent.ANY, consumeFilter);
					pane.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterAddition);
					pane.addEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterEdition);
					pane.addEventFilter(KeyEvent.KEY_PRESSED, rotateFilter);
				} else {
					pane.removeEventFilter(MouseEvent.ANY, consumeFilter);
					pane.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterAddition);
					pane.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressedFilterEdition);
					pane.removeEventFilter(KeyEvent.KEY_PRESSED, rotateFilter);
					pickedVertex = null;
					ObservableList<Node> nodes = pane.getChildren();
					Iterator<Node> iterator = nodes.iterator();
					while (iterator.hasNext()) {
						Node node = iterator.next();
						if (node instanceof Vertex) {
							controller.selectVertex((Vertex) node, DEFAULT_COLOR);
						} else {
							controller.selectEdge((Edge) node, DEFAULT_COLOR);
						}
					}
				}
			}
		});

		this.mode.setValue(EDITION);

		this.vertexName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				vertexName.setText(Util.parseLetter(vertexName.getText()));
			}
		});

		this.edgeSourceVertexName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				edgeSourceVertexName.setText(Util.parseLetter(edgeSourceVertexName.getText()));
			}
		});

		this.edgeTargetVertexName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				edgeTargetVertexName.setText(Util.parseLetter(edgeTargetVertexName.getText()));
			}
		});

		this.edgeValue.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				edgeValue.setText(Util.parseNumber(edgeValue.getText()));
			}
		});
	}

	public File open() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open");
		chooser.setInitialDirectory(new File("target/"));
		chooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		return chooser.showOpenDialog(this.getScene().getWindow());
	}

	public File saveAs() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Save As");
		chooser.setInitialDirectory(new File("target/"));
		chooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		return chooser.showSaveDialog(this.getScene().getWindow());
	}

	public void errorFile() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Grafx.APPLICATION_NAME);
		alert.setHeaderText("Error");
		alert.setContentText("The file must be an XML File.");
		alert.showAndWait();
	}

	public void errorVertex(int code) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Grafx.APPLICATION_NAME);
		alert.setHeaderText("Error");
		if (code == 1) {
			alert.setContentText("The name of vertex must be a letter.");
		} else if (code == 2) {
			alert.setContentText("The vertex already exists in the graph.");
		}
		alert.showAndWait();
	}

	public void errorEdge(int code) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Grafx.APPLICATION_NAME);
		alert.setHeaderText("Error");
		if (code == 1) {
			alert.setContentText("The name of vertices must be letters\nand the value of edge must be a number.");
		} else if (code == 2) {
			alert.setContentText("The vertices must be differents.");
		} else if (code == 3) {
			alert.setContentText("The edge already exists in the graph\nor the vertices don't exist in the graph.");
		}
		alert.showAndWait();
	}

	public void errorAlgorithm() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Grafx.APPLICATION_NAME);
		alert.setHeaderText("Error");
		alert.setContentText("The graph is not complete.");
		alert.showAndWait();
	}

	public void makeEditable(final Vertex vertex) {
		class T {
			double initialTranslateX, initialTranslateY, anchorX, anchorY;
		}

		final T t = new T();

		vertex.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (mode.getValue().equals(EDITION)) { // move
					pickedVertex = vertex;
					pane.requestFocus();
					controller.selectVertex(vertex, SELECT_COLOR);
					t.initialTranslateX = vertex.getTranslateX();
					t.initialTranslateY = vertex.getTranslateY();
					Point2D point = vertex.localToParent(event.getX(), event.getY());
					t.anchorX = point.getX();
					t.anchorY = point.getY();
				} else if (mode.getValue().equals(SUPPRESSION)) {
					ArrayList<Edge> edges = controller.removeVertex(vertex);
					pane.getChildren().removeAll(edges);
					pane.getChildren().remove(vertex);
				}
			}
		});

		vertex.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (mode.getValue().equals(EDITION)) { // move
					Point2D point = vertex.localToParent(event.getX(), event.getY());
					vertex.setTranslateX(t.initialTranslateX - t.anchorX + point.getX());
					vertex.setTranslateY(t.initialTranslateY - t.anchorY + point.getY());
					controller.moveEdges(vertex);
				}
			}
		});

		Rotate rotate = new Rotate(0, 0, 0, 0, new Point3D(0, 0, 1));
		vertex.getTransforms().add(0, rotate);
	}

	public void makeEditable(final Edge edge) {
		edge.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (mode.getValue().equals(EDITION)) { // selection
					controller.selectEdge(edge, SELECT_COLOR);
				} else if (mode.getValue().equals(SUPPRESSION)) {
					controller.removeEdge(edge);
					pane.getChildren().remove(edge);
				}
			}
		});
	}
}