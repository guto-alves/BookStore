package view;

import controller.AuthorController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Author;

public class AuthorView extends BorderPane {
	private TextField filterTextField;
	private TableView<Author> tableView;
	
	private TextField firstNameTextField;
	private TextField lastNameTextField;
	
	private Button saveButton;
	private Button cancelButton;

	private AuthorController controller;

	public AuthorView() {
		controller = new AuthorController();
		createLayout();
		initialize();
	}

	private void createLayout() {
		filterTextField = new TextField();
		filterTextField.setPrefWidth(220);
		filterTextField.setPromptText("Enter the name of the Author");

		tableView = new TableView<>();
		TableColumn<Author, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Author, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			firstNameColumn.setPrefWidth(newValue.doubleValue() / 2);
			lastNameColumn.setPrefWidth(newValue.doubleValue() / 2);
		});

		tableView.getColumns().addAll(firstNameColumn, lastNameColumn);

		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filter"), filterTextField); 
		flowPane.setPadding(new Insets(16));

		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		firstNameTextField = new TextField();
		firstNameTextField.setPrefWidth(220);
		lastNameTextField = new TextField();
		saveButton = new Button("Register");
		cancelButton = new Button("Cancel"); 

		GridPane.setHalignment(saveButton, HPos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10); 
		gridPane.setVgap(10);
		gridPane.addRow(0, new Label("First Name"), firstNameTextField);
		gridPane.addRow(1, new Label("Last Name"), lastNameTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 2);

		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		firstNameTextField.textProperty().bindBidirectional(
				controller.getFirstName());
		lastNameTextField.textProperty().bindBidirectional(
				controller.getLastName());
		
		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				controller.setAuthorSelected(newValue);
			});

		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(event -> controller.deleteAuthor());
		tableView.setContextMenu(new ContextMenu(deleteMenuItem));
	
		FilteredList<Author> filteredList = 
				new FilteredList<>(controller.getAuthorsList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			
			filteredList.setPredicate(author -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				if (author.getFirstName().toLowerCase().contains(text)
						|| author.getLastName().toLowerCase().contains(text)) {
					return true;
				}

				return false;
			});
			
		});
		SortedList<Author> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		controller.getInsertionMode()
			.addListener((observable, oldValue, insertionMode) -> {
				saveButton.setText(insertionMode ? "Register" : "Update");
			});
		
		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			controller.onActionButtonPressed();
		});
	}
}
