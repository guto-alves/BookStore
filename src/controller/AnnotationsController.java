package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AnnotationsController {
	private StringProperty buttonText = new SimpleStringProperty();
	private StringProperty text = new SimpleStringProperty();
	
	private File currentFile;
	private String currentTextSaved;

	public AnnotationsController(File file) {
		currentFile = file;
		
		if (currentFile == null) {
			buttonText.set("Save");
		} else {
			loadFile();
			buttonText.set("Saved");
		}
		
		text.addListener((observable, oldValue, newValue) -> {
			if (currentTextSaved == null) {
				return;
			}
			
			if (!currentTextSaved.equals(newValue)) {
				buttonText.set("Save");
			} else {
				buttonText.set("Saved");
			}
		});
	}
	
	public void loadFile() {
		try {
			currentTextSaved = 
					Files.lines(currentFile.toPath(), StandardCharsets.UTF_8)
						.collect(Collectors.joining("\n"));
			
			text.set(currentTextSaved);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() { 
		if (currentFile == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(
					new ExtensionFilter("Text Files", ".txt"));
			fileChooser.setInitialFileName("*.txt");
			currentFile = fileChooser.showSaveDialog(null);
		}

		if (currentFile != null) {
			try (Formatter output = new Formatter(currentFile, StandardCharsets.UTF_8, Locale.getDefault())) {
				output.format("%s%n", text.get());
				currentTextSaved = text.get();
				displayAlert(AlertType.INFORMATION, "Annotations Saved",
						"Annotations successfully saved.");
				buttonText.set("Saved");
			} catch (IOException e) {
				displayAlert(AlertType.ERROR, "Annotations Not Saved", 
						"Error saving file.");
				e.printStackTrace();
			}
		}
	}
	
	private void displayAlert(AlertType alertType, String title, 
			String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.show();
	}
	
	public StringProperty getText() {
		return text;
	}
	
	public StringProperty getButtonText() {
		return buttonText;
	}
}
