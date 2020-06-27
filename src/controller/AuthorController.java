package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Author;
import repository.AuthorRepository;

public class AuthorController {
	private ObservableList<Author> authorsList;
	private Author authorSelected;
	private boolean added;

	private final AuthorRepository authorRepository;

	public AuthorController() {
		authorRepository = new AuthorRepository();
		authorsList = FXCollections.observableArrayList();
		getAllAuthors();
	}

	private boolean hasInvalidFields(String firstName, String lastName) {
		return firstName.isBlank() || lastName.isBlank();
	}

	public int addAuthor(String firstName, String lastName) {
		if (hasInvalidFields(firstName, lastName)) {
			return 0;
		}
		
		int result = authorRepository.addAuthor(firstName, lastName);

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Autor Adicionado", "Autor salvo com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Autor Não Adicionado", "Erro ao adicionar o autor!");
		}

		getAllAuthors();
		added = true;
		return result;
	}

	public void updateAutor(String firstName, String lastName) {
		int result = authorRepository.updateAuthor(firstName, lastName, authorSelected);

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Autor Atualizado", "Autor atualizado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Autor Não Atualizado", "Erro ao atualizar o autor!");
		}

		getAllAuthors();
		added = true;
//		tableView.getSelectionModel().select(authorSelected);
	}

	public boolean deleteAuthor() {
		if (authorSelected == null) {
			return false;
		}

		int result = authorRepository.deleteAuthor(authorSelected);

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Autor Excluído", "Autor excluído com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Autor Não Excluído", "Não foi possível excluir o autor selecionado!");
		}

		authorsList.remove(authorSelected);

		return true;
	}

	private void getAllAuthors() {
		authorsList.clear();
		authorsList.setAll(authorRepository.getAllAuthors());
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}
	
	public ObservableList<Author> getAuthorsList() {
		return authorsList;
	}

	public Author getAuthorSelected() {
		return authorSelected;
	}

	public void setAuthorSelected(Author authorSelected) {
		this.authorSelected = authorSelected;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

}
