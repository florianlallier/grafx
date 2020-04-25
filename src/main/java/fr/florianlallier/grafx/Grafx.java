package fr.florianlallier.grafx;

import java.util.logging.Logger;

import fr.florianlallier.grafx.view.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Grafx extends Application {

	public final static Logger LOGGER = Logger.getLogger("grafx");

	public final static String APPLICATION_NAME = "grafx";
	private final static double WIDTH = 1200;
	private final static double HEIGHT = 800;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(APPLICATION_NAME);
		primaryStage.setScene(new Scene(new View(), WIDTH, HEIGHT));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}