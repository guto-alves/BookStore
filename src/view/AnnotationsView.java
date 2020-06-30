package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class AnnotationsView extends BorderPane {
	
	public AnnotationsView() {
		Button saveButton = new Button("Save"); 
		BorderPane.setAlignment(saveButton, Pos.CENTER);
		TextArea textArea = new TextArea();

		setPadding(new Insets(8));
		setTop(saveButton);
		setCenter(textArea);
	}
}
