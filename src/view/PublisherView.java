package view;

import controller.PublisherController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import model.Publisher;

public class PublisherView extends BorderPane { 
	private TextField nameTextField;
	private TextField phoneTextField;
	private TextField streetTextField;
	private TextField numberTextField;
	private TextField complementTextField;
	
	private Button saveButton;
	private Button cancelButton;

	private TextField filterTextField;
	private TableView<Publisher> tableView;

	private PublisherController controller;

	public PublisherView() {
		controller = new PublisherController();
		createLayout();
		initialize();
	}

	private void createLayout() {
		filterTextField = new TextField();
		filterTextField.setPrefWidth(250);
		FlowPane filterFlowPane = new FlowPane(8, 8, 
				new Label("Filter"), filterTextField);
		filterFlowPane.setPadding(new Insets(16));

		tableView = new TableView<>();

		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8)); 
		borderPane.setTop(filterFlowPane);
		borderPane.setCenter(tableView);

		nameTextField = new TextField();
		nameTextField.setPrefWidth(250);
		phoneTextField = new TextField(); 
		streetTextField = new TextField(); 
		numberTextField = new TextField();
		complementTextField = new TextField();
		saveButton = new Button("Register");
		cancelButton = new Button("Cancel");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10); 
		gridPane.addRow(0, new Label("Name"), nameTextField);
		gridPane.addRow(1, new Label("Phone"), phoneTextField);
		gridPane.addRow(2, new Label("Street"), streetTextField);
		gridPane.addRow(3, new Label("Number"), numberTextField);
		gridPane.addRow(4, new Label("Complement"), complementTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 5);

		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		TableColumn<Publisher, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Publisher, String> addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory((a) -> {
			if (a.getValue().getStreet().isBlank()) {
				return null;
			}
				
			return new SimpleStringProperty(String.format("%s, %s", 
					a.getValue().getStreet(),
					a.getValue().getNumber()));
		});

		TableColumn<Publisher, String> phoneColumn = new TableColumn<>("Phone");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			nameColumn.setPrefWidth(width / 3);
			addressColumn.setPrefWidth(width / 3);
			phoneColumn.setPrefWidth(width / 3);
		});

		tableView.getColumns().addAll(nameColumn, addressColumn, 
				phoneColumn);
		
		controller.getPublisherSelected()
			.addListener((ob, oldValue, newValue) -> {
				if (newValue == null) {
					nameTextField.setText("");
					streetTextField.setText(""); 
					numberTextField.setText(""); 
					complementTextField.setText(""); 
					phoneTextField.setText("");
					saveButton.setText("Register");
				} else {
					nameTextField.setText(newValue.getName());
					streetTextField.setText(newValue.getStreet()); 
					numberTextField.setText(newValue.getNumber()); 
					complementTextField.setText(newValue.getComplement()); 
					phoneTextField.setText(newValue.getPhone());
					saveButton.setText("Update");
				}
			});

		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ob, oldValue, newValue) -> {
					controller.onPublisherSelected(newValue);
				});

		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(event -> controller.deletePublisher());
		tableView.setContextMenu(new ContextMenu(deleteMenuItem));

		FilteredList<Publisher> filteredList = new FilteredList<>(
				controller.getPublishersList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(publisher -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				if (publisher.getName().toLowerCase().contains(text)
						|| publisher.getPhone().toLowerCase().contains(text)
						|| publisher.getStreet().toLowerCase().contains(text)
						|| publisher.getNumber().contains(text)
						|| publisher.getComplement().contains(text)) {
					return true;
				}

				return false;
			});
		});
		SortedList<Publisher> sortedList = new SortedList<Publisher>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		cancelButton.setOnAction(event -> 
			tableView.getSelectionModel().clearSelection()
		);

		saveButton.setOnAction(event -> 
			controller.onActionButtonPressed(nameTextField.getText(), 
					phoneTextField.getText(), streetTextField.getText(),
					numberTextField.getText(), complementTextField.getText())
		); 
		
		controller.getWarningInfo().addListener((ob, old, newValue) -> 
			AlertUtil.displayAlert(newValue));
	}
}
