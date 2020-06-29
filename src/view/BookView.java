package view;

import controller.BookController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Author;
import model.Publisher;
import model.Book;
import model.Category;

public class BookView extends BorderPane {
	private TextField isbnTextField;
	private TextField titleTextField;
	private TextArea descriptionTextArea;
	private TextField editionNumberTextField;
	private TextField yearTextField;
	private TextField priceTextField;
	private TextField copiesTextField;
	private ComboBox<Publisher> publishersComboBox;
	private ComboBox<Author> authorsComboBox;
	private Button addAuthorButton;
	private ListView<Author> authorsListView;
	private ComboBox<Category> categoriesComboBox;

	private Button cancelButton;
	private Button saveButton;

	private TextField filterTextField;
	private TableView<Book> tableView;

	private BookController controller;

	public BookView() {
		controller = new BookController();
		createLayout();
		initialize();
	}

	public void createLayout() {
		tableView = new TableView<Book>();
		filterTextField = new TextField();
		FlowPane flowPane = new FlowPane(8, 8, new Label("Filter by"), filterTextField);
		flowPane.setPadding(new Insets(16));
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		isbnTextField = new TextField();
		titleTextField = new TextField(); 
		descriptionTextArea = new TextArea();
		descriptionTextArea.setPrefWidth(200);
		descriptionTextArea.setPrefHeight(80);
		editionNumberTextField = new TextField();
		yearTextField = new TextField();
		priceTextField = new TextField();
		copiesTextField = new TextField();
		publishersComboBox = new ComboBox<>();
		categoriesComboBox = new ComboBox<>();
		authorsComboBox = new ComboBox<>();
		addAuthorButton = new Button(" >>> ");
		authorsListView = new ListView<>();
		authorsListView.setPrefHeight(50);
		saveButton = new Button("Register");
		cancelButton = new Button("Cancel");

		GridPane.setHalignment(saveButton, HPos.RIGHT);  

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER); 
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.addRow(0, new Label("ISBN"), isbnTextField);
		gridPane.addRow(1, new Label("Title"), titleTextField);
		gridPane.addRow(2, new Label("Description"), descriptionTextArea);
		gridPane.addRow(3, new Label("Edition Number"), editionNumberTextField);
		gridPane.addRow(4, new Label("Year"), yearTextField);
		gridPane.addRow(5, new Label("Price"), priceTextField);
		gridPane.addRow(6, new Label("Copies Number"), copiesTextField);
		gridPane.addRow(7, new Label("Publisher"), publishersComboBox);
		gridPane.addRow(8, new Label("Author(s)"),
				new VBox(new HBox(16, authorsComboBox, addAuthorButton), authorsListView));
		gridPane.addRow(9, new Label("Category"), categoriesComboBox);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 10);

		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		isbnTextField.textProperty().bindBidirectional(controller.getIsbn());
		titleTextField.textProperty().bindBidirectional(controller.getTitle());
		descriptionTextArea.textProperty().bindBidirectional(controller.getDescription());
		editionNumberTextField.textProperty().bindBidirectional(controller.getEditionNumber());
		yearTextField.textProperty().bindBidirectional(controller.getYear());
		priceTextField.textProperty().bindBidirectional(controller.getPrice());
		copiesTextField.textProperty().bindBidirectional(controller.getCopies());
		
		controller.getInsertionMode().addListener((observable, oldValue, newValue) -> { 
			if (newValue.booleanValue()) { 
				saveButton.setText("Register");
			} else {
				saveButton.setText("Update");
			}
		});
		
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

		TableColumn<Book, Integer> copiesColumn = new TableColumn<>("Copies");
		copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copies"));

		tableView.getColumns().addAll(isbnColumn, titleColumn,
				editionNumberColumn, yearColumn, priceColumn,
				copiesColumn);

		tableView.widthProperty().addListener((o, oldValue, newValue) -> {
			int totalColumn = tableView.getColumns().size();
			double width = newValue.doubleValue();

			for (TableColumn<Book, ?> column : tableView.getColumns()) {
				column.setPrefWidth(width / totalColumn);
			}
			
			isbnColumn.setPrefWidth(width * 0.22);
			titleColumn.setPrefWidth(width * 0.34);
			editionNumberColumn.setPrefWidth(width * 0.10);
			yearColumn.setPrefWidth(width * 0.14);
			priceColumn.setPrefWidth(width * 0.10);
			copiesColumn.setPrefWidth(width * 0.10);
		});

		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				controller.setBookSelected(newValue);
			});

		MenuItem excluirMenuItem = new MenuItem("Delete");
		excluirMenuItem.setOnAction(event -> controller.deleteBook());
		tableView.setContextMenu(new ContextMenu(excluirMenuItem));

		FilteredList<Book> filteredData = new FilteredList<>(controller.getBooksList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(book -> {
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
		SortedList<Book> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);

		publishersComboBox.setItems(controller.getPublishers());
		publishersComboBox.valueProperty().bindBidirectional(
				controller.getPublisherSelected());
		
		authorsComboBox.setItems(controller.getAuthors());
		
		authorsListView.setItems(controller.getAuthorsSelected());
		
		addAuthorButton.setOnAction(event -> {
			Author author = authorsComboBox.getSelectionModel().getSelectedItem();

			if (author == null || authorsListView.getItems().contains(author)) { 
				return;
			}

			authorsListView.getItems().add(author);
			authorsListView.scrollTo(author);
			authorsListView.getSelectionModel().selectLast();
		});
		MenuItem removeMenuItem = new MenuItem("Remove");
		removeMenuItem.setAccelerator(KeyCombination.keyCombination("R"));
		removeMenuItem.setOnAction(event -> {
			Author author = authorsListView.getSelectionModel().getSelectedItem();

			if (author == null) {
				return;
			}

			authorsListView.getItems().remove(author);
			authorsListView.getSelectionModel().selectLast();
		});
		authorsListView.setContextMenu(new ContextMenu(removeMenuItem));

		categoriesComboBox.setItems(controller.getCategories());
		categoriesComboBox.valueProperty().bindBidirectional(
				controller.getCategorySelected());

		cancelButton.setOnAction(event -> tableView.getSelectionModel().clearSelection());

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Register")) {
				controller.addBook();
			} else {
				controller.updateBook();
			}
		});
	}
}
