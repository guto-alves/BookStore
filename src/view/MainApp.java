package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.Database;
import view.LoginView;
import view.MainMenuView;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		LoginView loginView = new LoginView(result -> {
			if (result) {
				Scene scene = new Scene(new MainMenuView().getRoot());
				stage.setMaximized(true);
				stage.setTitle("Book Store");
				stage.setScene(scene);
				stage.setOnCloseRequest(event -> Database.close());
				stage.show();
			}
			return null;
		});

		loginView.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
