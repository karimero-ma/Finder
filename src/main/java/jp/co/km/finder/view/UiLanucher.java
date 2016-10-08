package jp.co.km.finder.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiLanucher extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/MainFrame.fxml"));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		scene.getStylesheets().add(getClass().getResource("/view/Application.css").toExternalForm());

		stage.show();
	}
}
