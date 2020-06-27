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
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Author;

public class AuthorView {
	private BorderPane root = new BorderPane();

	private TextField filterTextField;
	private TableView<Author> tableView;
	
	private TextField firstNameTextField;
	private TextField lastNameTextField;
	private Button saveButton;
	private Button cancelButton;

	private AuthorController authorController;

	public AuthorView() {
		authorController = new AuthorController();
		createLayout();
		initListeners();
	}

	private void createLayout() {
		filterTextField = new TextField();
		filterTextField.setPromptText("Digite o nome do Autor");

		tableView = new TableView<>();
		TableColumn<Author, String> firstNameColumn = new TableColumn<>("Nome");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Author, String> lastNameColumn = new TableColumn<>("Sobrenome");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			firstNameColumn.setPrefWidth(newValue.doubleValue() / 2);
			lastNameColumn.setPrefWidth(newValue.doubleValue() / 2);
		});

		tableView.getColumns().addAll(firstNameColumn, lastNameColumn);

		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filtrar"), filterTextField);
		flowPane.setPadding(new Insets(16));

		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		firstNameTextField = new TextField();
		lastNameTextField = new TextField();
		saveButton = new Button("Adicionar");
		cancelButton = new Button("Cancelar");

		GridPane.setHalignment(saveButton, HPos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.addRow(0, new Label("Nome"), firstNameTextField);
		gridPane.addRow(1, new Label("Sobrenome"), lastNameTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 2);

		root.setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initListeners() {
		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			authorController.setAuthorSelected(newValue);

			if (newValue == null || authorController.isAdded()) {
				authorController.setAdded(false);
				firstNameTextField.setText("");
				lastNameTextField.setText("");
				saveButton.setText("Adicionar");
			} else {
				firstNameTextField.setText(newValue.getFirstName());
				lastNameTextField.setText(newValue.getLastName());
				saveButton.setText("Atualizar");
			}
		});

		MenuItem deleteMenuItem = new MenuItem("Excluir");
		deleteMenuItem.setAccelerator(KeyCombination.keyCombination("R"));
		deleteMenuItem.setOnAction(event -> authorController.deleteAuthor());
		tableView.setContextMenu(new ContextMenu(deleteMenuItem));
		tableView.setItems(authorController.getAuthorsList());

		FilteredList<Author> filteredList = new FilteredList<>(tableView.getItems());
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

		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			firstNameTextField.setText(firstNameTextField.getText().strip());
			lastNameTextField.setText(lastNameTextField.getText().strip());

			if (saveButton.getText().contains("Adicionar")) {
				authorController.addAuthor(
						firstNameTextField.getText(), lastNameTextField.getText());
				tableView.getSelectionModel().selectLast();
			} else {
				authorController.updateAutor(
						firstNameTextField.getText(), lastNameTextField.getText());
			}
		});
	}

	public BorderPane getRoot() {
		return root;
	}
	
}
