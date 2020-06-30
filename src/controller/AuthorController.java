package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Author;
import persistence.AuthorDaoImpl;

public class AuthorController {
	private StringProperty firstName = new SimpleStringProperty();
	private StringProperty lastName = new SimpleStringProperty();

	private ObservableList<Author> authorsList;
	private ObjectProperty<Author> authorSelected = new SimpleObjectProperty<>();

	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);

	private final AuthorDaoImpl authorRepository;

	public AuthorController() {
		authorRepository = new AuthorDaoImpl();
		authorsList = FXCollections.observableArrayList();
		getAllAuthors();
	}

	public void setAuthorSelected(Author author) {
		authorSelected.set(author);
		
		if (author == null) {
			firstName.set("");
			lastName.set("");
			insertionMode.set(true);
		} else {
			firstName.set(author.getFirstName());
			lastName.set(author.getLastName());
			insertionMode.set(false);
		}
	}

	public void onActionButtonPressed() {
		if (insertionMode.get()) {
			addAuthor();
		} else {
			updateAutor();
		}
	}
	
	public void addAuthor() {
		if (hasInvalidFields()) {
			return;
		}

		Author author = new Author(firstName.get(), lastName.get());

		int result = authorRepository.addAuthor(author);

		if (result == 1) {
			getAllAuthors();
			setAuthorSelected(null);

			displayAlert(AlertType.INFORMATION, "Author Added",
					"Author successfully added.");
		} else {
			displayAlert(AlertType.ERROR, "Author Not Added", 
					"Unable to add author.");
		}
	}

	public void updateAutor() {
		if (hasInvalidFields()) {
			return;
		}
		
		Author author = new Author(authorSelected.get().getId(), 
				firstName.get(), lastName.get());

		int result = authorRepository.updateAuthor(author);

		if (result == 1) {
			getAllAuthors();
			displayAlert(AlertType.INFORMATION, "Author Updated",
					"Author successfully updated.");
		} else {
			displayAlert(AlertType.ERROR, "Author Not Updated", 
					"Unable to update author.");
		}
	}

	public void deleteAuthor() {
		if (authorSelected == null) {
			return;
		}

		int result = authorRepository.deleteAuthor(authorSelected.get());

		if (result == 1) {
			authorsList.remove(authorSelected.get());
			displayAlert(AlertType.INFORMATION, "Author Deleted",
					"Author successfully deleted.");
		} else {
			displayAlert(AlertType.ERROR, "Author Not Deleted",
					"Unable to delete author.");
		}
	}

	private void getAllAuthors() {
		authorsList.setAll(authorRepository.getAllAuthors());
	}
	
	private boolean hasInvalidFields() {
		firstName.set(firstName.get().strip());
		lastName.set(lastName.get().strip());
		return firstName.get().isBlank() || lastName.get().isBlank();
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText("Message");
		alert.setContentText(message);
		alert.show();
	}

	public StringProperty getFirstName() {
		return firstName;
	}
	
	public StringProperty getLastName() {
		return lastName;
	}
	
	public ObservableList<Author> getAuthorsList() {
		return authorsList;
	}

	public ObjectProperty<Author> getAuthorSelected() {
		return authorSelected;
	}

	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}
}
