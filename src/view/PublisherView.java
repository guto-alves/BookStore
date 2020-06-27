package view;

import controller.PublisherController;
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

public class PublisherView { 
	private BorderPane root;

	private TextField nameTextField;
	private TextField addressTextField;
	private TextField phoneTextField;
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
		root = new BorderPane();
		
		filterTextField = new TextField();
		filterTextField.setPrefWidth(250);
		FlowPane filterFlowPane = new FlowPane(8, 8, 
				new Label("Filtrar por"), filterTextField);
		filterFlowPane.setPadding(new Insets(16));

		tableView = new TableView<>();

		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(filterFlowPane);
		borderPane.setCenter(tableView);

		nameTextField = new TextField();
		nameTextField.setPrefWidth(200);
		addressTextField = new TextField(); 
		phoneTextField = new TextField();
		saveButton = new Button("Registrar");
		cancelButton = new Button("Cancelar");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.addRow(0, new Label("Nome"), nameTextField);
		gridPane.addRow(1, new Label("Endereço"), addressTextField);
		gridPane.addRow(2, new Label("Telefone"), phoneTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 3);

		root.setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		TableColumn<Publisher, String> nameColumn = new TableColumn<>("Nome");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Publisher, String> addressColumn = new TableColumn<>("Endereço");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		TableColumn<Publisher, String> phoneColumn = new TableColumn<>("Telefone");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			nameColumn.setPrefWidth(width / 3);
			addressColumn.setPrefWidth(width / 3);
			phoneColumn.setPrefWidth(width / 3);
		});

		tableView.getColumns().addAll(nameColumn, addressColumn, phoneColumn);
		
		controller.getPublisherSelected()
			.addListener((ob, oldValue, newValue) -> {
				if (newValue == null || controller.getInsertResult()) {
					nameTextField.setText("");
					addressTextField.setText(""); 
					phoneTextField.setText("");
					saveButton.setText("Registrar");
				} else {
					nameTextField.setText(newValue.getName());
					addressTextField.setText(newValue.getAddress());
					phoneTextField.setText(newValue.getPhone());
					saveButton.setText("Atualizar");
				}
			});

		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ob, oldValue, newValue) -> {
					controller.onPublisherSelected(newValue);
				});

		MenuItem deleteMenuItem = new MenuItem("Excluir");
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
						|| publisher.getAddress().toLowerCase().contains(text)
						|| String.valueOf(publisher.getPhone()).contains(text)) {
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
					addressTextField.getText(), phoneTextField.getText())
		); 
		
		controller.getWarningInfo().addListener((ob, old, newValue) -> 
			AlertUtil.displayAlert(newValue));
	}

	public BorderPane getRoot() {
		return root;
	}
	
}
