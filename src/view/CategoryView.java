package view;

import controller.CategoryController;
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
import model.Category;

public class CategoryView extends BorderPane {
	private TextField filterTextField;
	private TableView<Category> tableView;
	
	private TextField nameTextField;
	
	private Button saveButton; 
	private Button cancelButton;

	private CategoryController controller;

	public CategoryView() {
		controller = new CategoryController();
		createLayout();
		initialize();
	}

	private void createLayout() {
		filterTextField = new TextField();
		filterTextField.setPrefWidth(220);

		tableView = new TableView<>();
		TableColumn<Category, ?> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Category, ?> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			idColumn.setPrefWidth(newValue.doubleValue() / 2);
			nameColumn.setPrefWidth(newValue.doubleValue() / 2);
		});

		tableView.getColumns().addAll(idColumn, nameColumn);

		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filter"), filterTextField); 
		flowPane.setPadding(new Insets(16));

		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		nameTextField = new TextField();
		nameTextField.setPrefWidth(220);
		saveButton = new Button("Register");
		cancelButton = new Button("Cancel"); 

		GridPane.setHalignment(saveButton, HPos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10); 
		gridPane.setVgap(10); 
		gridPane.addRow(0, new Label("Name"), nameTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 1);

		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		nameTextField.textProperty().bindBidirectional(
				controller.getFirstName());
		
		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				controller.setCategorySelected(newValue);
			});

		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Delete"));
		deleteMenuItem.setOnAction(event -> controller.deleteCategory());
		tableView.setContextMenu(new ContextMenu(deleteMenuItem));

		FilteredList<Category> filteredList = 
				new FilteredList<>(controller.getCategorysList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			
			filteredList.setPredicate(category -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String filter = newValue.toLowerCase();

				if (String.valueOf(category.getId()).contains(filter)
						|| category.getName().toLowerCase().contains(filter)) {
					return true; 
				}

				return false;
			});
		});
		SortedList<Category> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		controller.getInsertionMode()
			.addListener((observable, oldValue, insertionMode) -> 
						saveButton.setText(insertionMode ? "Register" : "Update")
			);
		
		cancelButton.setOnAction(event -> 
			tableView.getSelectionModel().clearSelection()
		); 

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Register")) {
				controller.addCategory();
			} else {
				controller.updateAutor();
			}
		});
	}
}
