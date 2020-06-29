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
import persistence.BookDaoImpl;

public class BooksSelectionDialog {

	public interface BooksSelectionDialogListener {
		void onDialogClosed(List<Book> books);
	}

	private Dialog<ButtonType> dialog; 
	private BookDaoImpl bookDao = new BookDaoImpl();
	private TableView<Book> tableView = new TableView<>();

	public BooksSelectionDialog() {
		dialog = new Dialog<>();
		dialog.setWidth(800);
		dialog.setTitle("Select Books for Sale");
		dialog.setResizable(true);

		TextField filterTextField = new TextField();
		filterTextField.setPrefWidth(220);

		tableView = new TableView<>();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));

		TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));

		TableColumn<Book, Integer> editionNumberColumn = new TableColumn<>("Edition");
		editionNumberColumn.setCellValueFactory(new PropertyValueFactory<>("editionNumber"));

		TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		 
		TableColumn<Book, Integer> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

		tableView.getColumns().addAll(isbnColumn, titleColumn, 
				editionNumberColumn, yearColumn, priceColumn);
		
		tableView.widthProperty().addListener((o, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			isbnColumn.setPrefWidth(width * 0.225);
			titleColumn.setPrefWidth(width * 0.40);
			editionNumberColumn.setPrefWidth(width * 0.125);
			yearColumn.setPrefWidth(width * 0.125);
			priceColumn.setPrefWidth(width * 0.125);
		});

		ObservableList<Book> booksList = 
				FXCollections.observableArrayList(bookDao.getAllBooks());
		FilteredList<Book> filteredList = new FilteredList<>(booksList);
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(book  -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}
				
				String filter = newValue.toLowerCase();

				if (book.getIsbn().toLowerCase().contains(filter)
						|| book.getTitle().toLowerCase().contains(filter)
						|| book.getDescription().toLowerCase().contains(filter)
						|| book.getYear().toLowerCase().contains(filter)
						|| String.valueOf(book.getPrice()).contains(filter)
						|| book.getPublisher().getName().toLowerCase().contains(filter)
						|| book.getCategory().getName().toLowerCase().contains(filter)) {
					return true;
				}
				
				return false;
			});
		});
		SortedList<Book> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty()); 
		tableView.setItems(sortedList); 

		ButtonType buttonType = new ButtonType("Select", ButtonData.APPLY);
		dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);
		Node node = dialog.getDialogPane().lookupButton(buttonType);
		node.setDisable(true);
		node.disableProperty().bind(
				tableView.getSelectionModel().selectedItemProperty().isNull());

		FlowPane flowPane = new FlowPane(8, 8, new Label("Filter by"), filterTextField);
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
			listener.onDialogClosed(
					tableView.getSelectionModel().getSelectedItems());
		}
	}

}
