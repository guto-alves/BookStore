package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class AnotationsView {
	private BorderPane root;

	public AnotationsView() {
		Button saveButton = new Button("Salvar");
		BorderPane.setAlignment(saveButton, Pos.CENTER);
		TextArea textArea = new TextArea();

		root = new BorderPane();
		root.setPadding(new Insets(8));
		root.setTop(saveButton);
		root.setCenter(textArea);
	}

	public BorderPane getRoot() {
		return root;
	}
}
