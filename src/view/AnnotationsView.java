package view;

import java.io.File;

import controller.AnnotationsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class AnnotationsView extends BorderPane {
	private Button saveButton;
	private TextArea textArea;

	private AnnotationsController controller;

	public AnnotationsView(File file) {
		controller = new AnnotationsController(file);
		createLayout();
		initialize();
	}

	private void createLayout() {
		saveButton = new Button("Save");
		BorderPane.setAlignment(saveButton, Pos.CENTER);
		
		textArea = new TextArea();
		textArea.setWrapText(true);

		setPadding(new Insets(8));
		setTop(saveButton);
		setCenter(textArea);
	}

	private void initialize() {
		saveButton.textProperty().bindBidirectional(controller.getButtonText());
		textArea.textProperty().bindBidirectional(controller.getText());
		
		saveButton.setOnAction(event -> {
			controller.save();
		});
	}
}
