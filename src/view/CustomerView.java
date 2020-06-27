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

public class CustomerView {
	private BorderPane root = new BorderPane();

	private TextField nameTextField;
	private TextField rgTextField;
	private TextField cpfTextField;
	private TextField emailTextField;
	private TextField phoneTextField;
	private Button addPhoneButton;
	private ListView<String> phonesListView;

	private TextField streetTextField;
	private TextField numberTextField;
	private TextField complementTextField;
	private TextField postalCodeTextField;

	private Button saveButton;
	private Button cancelButton;

	private Text warningText;

	private TextField filterTextField;
	private TableView<Customer> tableView;

	private final CustomerController controller = new CustomerController();

	public CustomerView() {
		createLayout();
		initialize();
	}
	
	private void createLayout() {
		tableView = new TableView<>();
		filterTextField = new TextField();
		FlowPane flowPane = new FlowPane(8, 8, new Label("Filtrar por"), filterTextField);
		flowPane.setPadding(new Insets(16));
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8)); 
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHalignment(HPos.RIGHT); 
		gridPane.getColumnConstraints().add(columnConstraints);

		nameTextField = new TextField();
		nameTextField.setPrefWidth(200);
		rgTextField = new TextField();
		rgTextField.setTextFormatter(new TextFormatter<>(text -> { 
			text.setText(text.getText().matches("[0-9]") ? text.getText() : "");
			return text;
		}));
		
		cpfTextField = new TextField(); 
		emailTextField = new TextField();

		phoneTextField = new TextField();
		addPhoneButton = new Button("+");
		phonesListView = new ListView<>();
		phonesListView.setPrefHeight(50);

		VBox addressVBox = new VBox(8);
		addressVBox.setAlignment(Pos.TOP_CENTER);
		addressVBox.setBorder(new Border(
				new BorderStroke(Color.DEEPSKYBLUE, BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		Text text = new Text("Endereço");
		text.setFill(Color.DEEPSKYBLUE);

		GridPane addressGridPane = new GridPane();
		addressGridPane.setPadding(new Insets(16));
		addressGridPane.setAlignment(Pos.CENTER);
		addressGridPane.setVgap(10);
		addressGridPane.setHgap(10);
		GridPane.setColumnSpan(addressVBox, GridPane.REMAINING);
		streetTextField = new TextField();
		numberTextField = new TextField();
		complementTextField = new TextField();
		postalCodeTextField = new TextField();
		addressGridPane.addRow(0, new Label("Logradouro"), streetTextField);
		addressGridPane.addRow(1, new Label("Número"), numberTextField);
		addressGridPane.addRow(2, new Label("Compl."), complementTextField);
		addressGridPane.addRow(3, new Label("CEP"), postalCodeTextField);
		addressVBox.getChildren().addAll(text, addressGridPane);

		saveButton = new Button("Adicionar");
		cancelButton = new Button("Cancelar");

		warningText = new Text();
		warningText.setFill(Color.FIREBRICK);
		GridPane.setHalignment(warningText, HPos.CENTER);

		Label phonesLabel = new Label("Telefone(s)");
		GridPane.setValignment(phonesLabel, VPos.TOP);
		gridPane.addRow(0, new Label("Nome"), nameTextField);
		gridPane.addRow(1, new Label("RG"), rgTextField);
		gridPane.addRow(2, new Label("CPF"), cpfTextField);
		gridPane.addRow(3, new Label("Email"), emailTextField);
		gridPane.addRow(4, phonesLabel,
				new VBox(8, new HBox(phoneTextField, addPhoneButton), 
						phonesListView));
		gridPane.addRow(5, addressVBox);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 6);
		gridPane.add(warningText, 1, 7);

		root.setCenter(new SplitPane(borderPane, gridPane));
	}
	
	private void initialize() {
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Nome");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Customer, String> rgColumn = new TableColumn<>("RG");
		rgColumn.setCellValueFactory(new PropertyValueFactory<>("rg"));

		TableColumn<Customer, String> cpfColumn = new TableColumn<>("CPF");
		cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));

		TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		tableView.getColumns().setAll(nameColumn, rgColumn, cpfColumn, emailColumn);

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			nameColumn.setPrefWidth(width * 0.25);
			rgColumn.setPrefWidth(width * 0.25);
			cpfColumn.setPrefWidth(width * 0.25);
			emailColumn.setPrefWidth(width * 0.25);
		});

		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			controller.setCustomerSelected(newValue);
		
			if (newValue == null) {
				nameTextField.setText("");
				rgTextField.setText("");
				cpfTextField.setText("");
				emailTextField.setText("");
				phonesListView.getItems().clear();
				
				streetTextField.setText("");
				numberTextField.setText("");
				complementTextField.setText("");
				postalCodeTextField.setText("");
				
				saveButton.setText("Adicionar");
			} else {
				nameTextField.setText(newValue.getName());
				rgTextField.setText(newValue.getRg());
				cpfTextField.setText(newValue.getCpf());
				emailTextField.setText(newValue.getEmail());
				phonesListView.getItems().setAll(controller.getPhones());
				streetTextField.setText(newValue.getStreet());
				numberTextField.setText(String.valueOf(
						newValue.getNumber()));
				complementTextField.setText(newValue.getComplement());
				postalCodeTextField.setText(newValue.getPostalCode());
				
				saveButton.setText("Atualizar");
			}
		});

		MenuItem excluirMenuItem = new MenuItem("Excluir");
		excluirMenuItem.setAccelerator(KeyCombination.keyCombination("DELETE"));
		excluirMenuItem.setOnAction(event -> controller.deleteCustomer());
		tableView.setContextMenu(new ContextMenu(excluirMenuItem));

		FilteredList<Customer> filteredList = new FilteredList<>(
				controller.getCustomersList());
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(customer -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				try {
					if (customer.getRg().toLowerCase().contains(text)
							|| customer.getCpf().toLowerCase().contains(text)
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
			phoneTextField.setText("");
		});
		addPhoneButton.setOnAction(event -> {
			phonesListView.getItems().add(phoneTextField.getText());
			phoneTextField.setText("");
		});
		
		MenuItem removePhoneMenuItem = new MenuItem("Remover");
		removePhoneMenuItem.setOnAction(event -> {
			phonesListView.getItems().remove(
					phonesListView.getSelectionModel().getSelectedIndex());
		});
		phonesListView.setContextMenu(new ContextMenu(removePhoneMenuItem));
		
		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Adicionar")) {
				controller.addCustomer(
						rgTextField.getText(), cpfTextField.getText(),
						nameTextField.getText(), emailTextField.getText(), 
						phonesListView.getItems(),
						streetTextField.getText(), numberTextField.getText(),
						complementTextField.getText(), postalCodeTextField.getText());
			} else {
				controller.updateCustomer(
						rgTextField.getText(), cpfTextField.getText(),
						nameTextField.getText(), emailTextField.getText(), 
						phonesListView.getItems(),
						streetTextField.getText(), numberTextField.getText(),
						complementTextField.getText(), postalCodeTextField.getText());
			}
		});
		
		warningText.textProperty().bind(controller.getWarning());
	}

	public BorderPane getRoot() {
		return root;
	}

}
