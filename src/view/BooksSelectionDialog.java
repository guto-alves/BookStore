package view;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.Book;
import repository.BookRepository;

public class BooksSelectionDialog {

	public interface BooksSelectionDialogListener {
		void onDialogClosed(List<Book> books);
	}

	private Dialog<ButtonType> dialog;
	private BookRepository bookRepository = new BookRepository();
	private TableView<Book> tableView = new TableView<>();

	public BooksSelectionDialog() {
		dialog = new Dialog<>();
		dialog.setTitle("Selecione os Livros do Empréstimo");
		dialog.setResizable(true);

		TextField filterTextField = new TextField();

		tableView = new TableView<>();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
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
			double width = newValue.doubleValue();
			isbnColumn.setPrefWidth(width / 4);
			titleColumn.setPrefWidth(width / 4);
			editionNumberColumn.setPrefWidth(width / 4);
			yearColumn.setPrefWidth(width / 4);
		});

		ObservableList<Book> booksList = FXCollections.observableArrayList();
		booksList.setAll(bookRepository.getAllBooks());
		FilteredList<Book> filteredList = new FilteredList<>(booksList);
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(book  -> {
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
		SortedList<Book> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList); 

		ButtonType buttonType = new ButtonType("Selecionar", ButtonData.APPLY);
		dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);
		Node node = dialog.getDialogPane().lookupButton(buttonType);
		node.setDisable(true);
		node.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());

		FlowPane flowPane = new FlowPane(8, 8, new Label("Filtrar por"), filterTextField);
		flowPane.setPadding(new Insets(16));
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		dialog.getDialogPane().setContent(borderPane);
	}

	public void show(BooksSelectionDialogListener listener) {
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isPresent() && result.get() != ButtonType.CANCEL) { 
			listener.onDialogClosed(tableView.getSelectionModel().getSelectedItems());
		}
	}

}
