package controller;

import java.util.List;

import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Author; 
import model.Publisher;
import model.Book;
import repository.AuthorBookRepository;
import repository.AuthorRepository;
import repository.PublisherRepository;
import repository.BookRepository;

public class BookController {
	private TextField isbnTextField;
	private TextField titleTextField;
	private TextArea descriptionTextArea;
	private TextField editionNumberTextField;
	private TextField yearTextField;
	private ComboBox<Publisher> publishersComboBox;
	private ComboBox<Author> authorsComboBox;
	private ListView<Author> authorsListView;
	private Button addAuthorButton;
	private Button cancelButton;
	private Button saveButton;
	
	private TextField filtroTextField;
	private TableView<Book> tableView;
	private Book bookSelected;
	
	private ObservableList<Book> booksList;
	private ObservableList<Publisher> publishersList;
	private ObservableList<Author> authorsList;
	
	private final BookRepository bookRepository;
	private final PublisherRepository publisherRepository;
	private final AuthorRepository authorRepository;
	private final AuthorBookRepository authorBookRepository;

	public BookController(TextField isbnTextField, TextField titleTextField, 
			TextArea descriptionTextArea, TextField editionNumberTextField, 
			TextField yearTextField, ComboBox<Publisher> publishersComboBox, 
			ComboBox<Author> authorsComboBox, ListView<Author> authorsListView, 
			Button addAuthorButton, Button cancelButton, Button saveButton, 
			TextField filtroTextField, TableView<Book> tableView) {
		this.isbnTextField = isbnTextField;
		this.titleTextField = titleTextField;
		this.descriptionTextArea = descriptionTextArea;
		this.editionNumberTextField = editionNumberTextField;
		this.yearTextField = yearTextField;
		this.publishersComboBox = publishersComboBox;
		this.authorsComboBox = authorsComboBox;
		this.authorsListView = authorsListView;
		this.addAuthorButton = addAuthorButton;
		this.cancelButton = cancelButton;
		this.saveButton = saveButton;
		this.filtroTextField = filtroTextField;
		this.tableView = tableView;
		bookRepository = new BookRepository();
		publisherRepository = new PublisherRepository();
		authorRepository = new AuthorRepository();
		authorBookRepository = new AuthorBookRepository();
		initialize();
	}

	private void initialize() {
		TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));

		TableColumn<Book, String> titleColumn = new TableColumn<>("Título");
		titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

		TableColumn<Book, Integer> editionNumberColumn = new TableColumn<>("Edição");
		editionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("editionNumber"));

		TableColumn<Book, Integer> yearColumn = new TableColumn<>("Ano");
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

		tableView.getColumns().addAll(isbnColumn, titleColumn, 
				editionNumberColumn, yearColumn);
		
		tableView.widthProperty().addListener((o, oldValue, newValue) -> {
			isbnColumn.setPrefWidth(tableView.getWidth() / 4);
			titleColumn.setPrefWidth(tableView.getWidth() / 4);
			editionNumberColumn.setPrefWidth(tableView.getWidth() / 4);
			yearColumn.setPrefWidth(tableView.getWidth() / 4);
		});
		
		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				bookSelected = newValue;
				
				if (bookSelected == null) {
					isbnTextField.setText("");
					titleTextField.setText("");
					descriptionTextArea.setText("");
					editionNumberTextField.setText("");
					yearTextField.setText("");
					authorsList.addAll(authorsListView.getItems());
					authorsListView.getItems().clear();
					saveButton.setText("Adicionar");
				} else {
					isbnTextField.setText(bookSelected.getIsbn());
					titleTextField.setText(bookSelected.getTitle());
					descriptionTextArea.setText(bookSelected.getDescription());
					editionNumberTextField.setText(String.valueOf(bookSelected.getEditionNumber()));
					yearTextField.setText(String.valueOf(bookSelected.getYear()));
					publishersComboBox.getSelectionModel().select(bookSelected.getPublisher());
					
					authorsComboBox.getItems().addAll(authorsListView.getItems());
					List<Author> authors = 
							authorBookRepository.getAllAuthorsOfTheBook(bookSelected.getIsbn());
					authorsComboBox.getItems().removeAll(authors);
					authorsListView.getItems().setAll(authors);
					
					saveButton.setText("Atualizar");
				}
			});
		
		MenuItem excluirMenuItem = new MenuItem("Excluir");
		excluirMenuItem.setOnAction(event -> {
				if (bookSelected == null) {
					return;
				}
				
				deleteBook();
		});
		tableView.setContextMenu(new ContextMenu(excluirMenuItem));
		
		booksList = FXCollections.observableArrayList();
		tableView.setItems(booksList);
		FilteredList<Book> filteredData = new FilteredList<>(booksList);
		filtroTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(book  -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (book.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (book.getDescription().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (book.getPublisher().getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				}
				
				return false;
			});
		});
		SortedList<Book> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);
		getAllBooks();
		
		publishersList = FXCollections.observableArrayList();
		publishersList.addAll(publisherRepository.getAllPublishers());
		publishersComboBox.setItems(publishersList);

		authorsList = FXCollections.observableArrayList(authorRepository.getAllAuthors());
		authorsComboBox.setItems(authorsList);
		addAuthorButton.setOnAction(event -> {
			Author author = authorsComboBox.getSelectionModel().getSelectedItem();
			
			if (author == null) {
				return;  
			}
			
			authorsComboBox.getItems().remove(author);
			authorsListView.getItems().add(author);
			authorsListView.getSelectionModel().selectLast();
		});
		MenuItem removeMenuItem = new MenuItem("Remover");
		removeMenuItem.setAccelerator(KeyCombination.keyCombination("R"));
		removeMenuItem.setOnAction(event -> {
			Author author = authorsListView.getSelectionModel().getSelectedItem();
	
			if (author == null) {
				return;
			}
			
			authorsComboBox.getItems().add(author);
			authorsListView.getItems().remove(author);
			authorsListView.getSelectionModel().selectLast();
		});
		authorsListView.setContextMenu(new ContextMenu(removeMenuItem));
		
		cancelButton.setOnAction(event -> 
			tableView.getSelectionModel().clearSelection());
		
		saveButton.setOnAction(event -> {
			if (isValidFields()) {
				return;
			}
			
			if (saveButton.getText().contains("Adicionar")) {
				addBook();
			} else {
				updateBook();
			}
		});
	}
	
	private boolean isValidFields() {
		return isbnTextField.getText().isBlank() || 
				titleTextField.getText().isBlank() ||
				editionNumberTextField.getText().isBlank() ||
				yearTextField.getText().isBlank();
	}

	private void addBook() {
		int result = bookRepository.addBook(isbnTextField.getText(), titleTextField.getText(),
				descriptionTextArea.getText(), editionNumberTextField.getText(), yearTextField.getText(),
				publishersComboBox.getSelectionModel().getSelectedItem().getName());
		
		authorBookRepository.add(isbnTextField.getText(), authorsListView.getItems());

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Livro Salvo", "Livro salvo com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Livro Não Salvo", "Erro ao salvar book!");
		}

		getAllBooks();
		tableView.getSelectionModel().selectLast();
	}

	private void updateBook() {
		int result = bookRepository.updateBook(
				isbnTextField.getText(), titleTextField.getText(), 
				descriptionTextArea.getText(), editionNumberTextField.getText(),
				yearTextField.getText(), 
				publishersComboBox.getSelectionModel().getSelectedItem().getName(),
				bookSelected.getIsbn());
		
		authorBookRepository.add(isbnTextField.getText(), authorsListView.getItems());

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Livro Atualizado",
					"Livro atualizado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Livro Não Atualizado",
					"Não foi possível atualizar o book selecionado!");
		}

		getAllBooks();
	}
	
	private void deleteBook() {
		int result = bookRepository.deleteBook(bookSelected.getIsbn());
		
		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Livro Excluído",
					"Livro excluído com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Livro Não Excluído", 
					"Não foi possível excluir a book selecionado!");
		}
		
		booksList.remove(tableView.getSelectionModel().getSelectedIndex());
	}
	
	private void getAllBooks() {
		booksList.clear();
		booksList.addAll(bookRepository.getAllBooks());
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}
}
