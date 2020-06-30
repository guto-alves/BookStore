package view;

import controller.CustomerController;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Customer;

public class CustomerView extends BorderPane {
	private TextField nameTextField;
	private TextField cpfTextField;
	private TextField emailTextField;
	private TextField phoneTextField;
	private Button addPhoneButton;
	private ListView<String> phonesListView;

	private TextField streetTextField;
	private TextField numberTextField;
	private TextField complementTextField;
	private TextField zipCodeTextField;

	private Button saveButton;
	private Button cancelButton;

	private Text warningText;

	private TextField filterTextField;
	private TableView<Customer> tableView;

	private final CustomerController controller;

	public CustomerView() {
		controller = new CustomerController();
		createLayout();
		initialize();
	}

	private void createLayout() {
		filterTextField = new TextField();
		filterTextField.setPrefWidth(220);
		FlowPane filterFlowPane = new FlowPane(8, 8, 
				new Label("Filter"), filterTextField);
		filterFlowPane.setPadding(new Insets(16));
		
		tableView = new TableView<>();
		
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(filterFlowPane);
		borderPane.setCenter(tableView);

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHalignment(HPos.RIGHT);
		gridPane.getColumnConstraints().add(columnConstraints);

		nameTextField = new TextField();
		nameTextField.setPrefWidth(220);

		cpfTextField = new TextField();
		cpfTextField.setTextFormatter(new TextFormatter<>(text -> {
			text.setText(text.getText().matches("[0-9]+") ? text.getText() : ""); 
			return text;
		})); 
		
		emailTextField = new TextField();

		phoneTextField = new TextField();
		addPhoneButton = new Button("+");
		phonesListView = new ListView<>();
		phonesListView.setPrefHeight(50);

		VBox addressVBox = new VBox(8);
		addressVBox.setAlignment(Pos.TOP_CENTER);
		addressVBox.setBorder(new Border(
				new BorderStroke(
						Color.BLUE, BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		Text text = new Text("Address");
		text.setFill(Color.BLUE);

		GridPane addressGridPane = new GridPane();
		addressGridPane.setPadding(new Insets(16));
		addressGridPane.setAlignment(Pos.CENTER);
		addressGridPane.setVgap(10);
		addressGridPane.setHgap(10);
		GridPane.setColumnSpan(addressVBox, GridPane.REMAINING);
		streetTextField = new TextField();
		numberTextField = new TextField();
		complementTextField = new TextField();
		zipCodeTextField = new TextField();
		addressGridPane.addRow(0, new Label("Street"), streetTextField);
		addressGridPane.addRow(1, new Label("Number"), numberTextField);
		addressGridPane.addRow(2, new Label("Compl."), complementTextField);
		addressGridPane.addRow(3, new Label("Zip Code"), zipCodeTextField);
		addressVBox.getChildren().addAll(text, addressGridPane);

		saveButton = new Button("Register");
		cancelButton = new Button("Cancel");

		warningText = new Text();
		warningText.setFill(Color.FIREBRICK);
		GridPane.setHalignment(warningText, HPos.CENTER);

		Label phonesLabel = new Label("Phone(s)"); 
		GridPane.setValignment(phonesLabel, VPos.TOP);
		gridPane.addRow(0, new Label("Name"), nameTextField);
		gridPane.addRow(1, new Label("CPF"), cpfTextField);
		gridPane.addRow(2, new Label("Email"), emailTextField);
		gridPane.addRow(3, phonesLabel, new VBox(8, new HBox(phoneTextField, addPhoneButton), phonesListView));
		gridPane.addRow(4, addressVBox);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 5);
		gridPane.add(warningText, 1, 6);

		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Customer, String> cpfColumn = new TableColumn<>("CPF");
		cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));

		TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		tableView.getColumns().setAll(nameColumn, cpfColumn, emailColumn);

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			nameColumn.setPrefWidth(width * 0.34);
			cpfColumn.setPrefWidth(width * 0.33);
			emailColumn.setPrefWidth(width * 0.33);
		});

		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
				controller.setCustomerSelected(newValue);
	
				if (newValue == null) {
					nameTextField.setText("");
					cpfTextField.setText("");
					emailTextField.setText("");
					streetTextField.setText("");
					numberTextField.setText("");
					complementTextField.setText("");
					zipCodeTextField.setText("");
					
					phonesListView.getItems().clear();
	
					saveButton.setText("Register");
				} else {
					nameTextField.setText(newValue.getName());
					cpfTextField.setText(newValue.getCpf());
					emailTextField.setText(newValue.getEmail());
					streetTextField.setText(newValue.getStreet());
					numberTextField.setText(String.valueOf(newValue.getNumber()));
					complementTextField.setText(newValue.getComplement());
					zipCodeTextField.setText(newValue.getZipCode());
					
					phonesListView.getItems().setAll(controller.getAllPhones()); 
	
					saveButton.setText("Update");
				}
			});

		MenuItem excluirMenuItem = new MenuItem("Delete");
		excluirMenuItem.setAccelerator(KeyCombination.keyCombination("DELETE"));
		excluirMenuItem.setOnAction(event -> controller.deleteCustomer());
		tableView.setContextMenu(new ContextMenu(excluirMenuItem));

		FilteredList<Customer> filteredList = new FilteredList<>(controller.getCustomersList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(customer -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				try {
					if (customer.getCpf().toLowerCase().contains(text)
							|| customer.getName().toLowerCase().contains(text)
							|| customer.getEmail().toLowerCase().contains(text)) {
						return true;
					}
				} catch (NullPointerException ignored) {
				}

				return false;
			});
		});
		SortedList<Customer> sortedList = new SortedList<Customer>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		phoneTextField.setOnAction(event -> {
			phonesListView.getItems().add(phoneTextField.getText());
			phonesListView.scrollTo(phoneTextField.getText());
			phonesListView.getSelectionModel().select(phoneTextField.getText());
			phoneTextField.setText("");
		});
		addPhoneButton.setOnAction(event -> {
			phonesListView.getItems().add(phoneTextField.getText());
			phonesListView.scrollTo(phoneTextField.getText());
			phonesListView.getSelectionModel().select(phoneTextField.getText());
			phoneTextField.setText("");
		});

		MenuItem removePhoneMenuItem = new MenuItem("Remove");
		removePhoneMenuItem.setOnAction(event -> {
			phonesListView.getItems().remove(
					phonesListView.getSelectionModel().getSelectedIndex());
		});
		phonesListView.setContextMenu(new ContextMenu(removePhoneMenuItem));

		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Register")) {
				controller.addCustomer(cpfTextField.getText(), nameTextField.getText(),
						emailTextField.getText(), phonesListView.getItems(), streetTextField.getText(),
						numberTextField.getText(), complementTextField.getText(), zipCodeTextField.getText());
			} else {
				controller.updateCustomer(cpfTextField.getText(), nameTextField.getText(),
						emailTextField.getText(), phonesListView.getItems(), streetTextField.getText(),
						numberTextField.getText(), complementTextField.getText(), zipCodeTextField.getText());
			}
		});

		warningText.textProperty().bind(controller.getWarning());
	}

}
