package view;

import controller.EmployeeController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Employee;
import model.Role;

public class EmployeeView {
	private BorderPane root = new BorderPane();

	private TextField nameTextField;
	private TextField phoneTextField;
	private ComboBox<Role> rolesComboBox;
	private TextField emailTextField;
	private TextField passwordTextField;
	private Button saveButton;
	private Button updateButton;
	private Button cancelButton;

	private TextField filtedTextField;
	private TableView<Employee> tableView;

	private EmployeeController controller;

	public EmployeeView() {
		controller = new EmployeeController();
		createLayout();
		initialize();
	}

	private void createLayout() {
		root = new BorderPane();

		tableView = new TableView<>();
		filtedTextField = new TextField();
		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filtrar por"), filtedTextField);
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
		phoneTextField = new TextField();
		rolesComboBox = new ComboBox<>();
		emailTextField = new TextField();
		PasswordField senhaPasswordField = new PasswordField();
		passwordTextField = new TextField();
		saveButton = new Button("Registrar");
		updateButton = new Button("Atualizar");
		updateButton.setManaged(false);
		cancelButton = new Button("Cancelar");

		CheckBox mostrarSenhaCheckBox = new CheckBox();
		passwordTextField.managedProperty().bind(mostrarSenhaCheckBox.selectedProperty());
		passwordTextField.visibleProperty().bind(mostrarSenhaCheckBox.selectedProperty());
		senhaPasswordField.managedProperty().bind(mostrarSenhaCheckBox.selectedProperty().not());
		senhaPasswordField.visibleProperty().bind(mostrarSenhaCheckBox.selectedProperty().not());
		senhaPasswordField.textProperty().bindBidirectional(passwordTextField.textProperty());

		gridPane.addRow(0, new Label("Nome"), nameTextField);
		gridPane.addRow(1, new Label("Telefone"), phoneTextField);
		gridPane.addRow(2, new Label("Cargo"), rolesComboBox);
		gridPane.addRow(3, new Label("Email"), emailTextField);
		gridPane.addRow(4, new Label("Senha"), new VBox(8, 
				new HBox(senhaPasswordField, passwordTextField),
				new HBox(8, new Label("Mostrar Senha?"), mostrarSenhaCheckBox)));
		gridPane.add(new HBox(16, cancelButton, updateButton, saveButton), 1, 5);

		root.setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() {
		TableColumn<Employee, String> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Employee, String> nameColumn = new TableColumn<>("Nome");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Employee, String> phoneColumn = new TableColumn<>("Telefone");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

		TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		TableColumn<Employee, String> roleColumn = new TableColumn<>("Cargo");
		roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

		tableView.getColumns().setAll(idColumn, nameColumn, phoneColumn, 
				emailColumn, roleColumn);

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			idColumn.setPrefWidth(width * 0.1);
			nameColumn.setPrefWidth(width * 0.225);
			phoneColumn.setPrefWidth(width * 0.225);
			emailColumn.setPrefWidth(width * 0.225);
			roleColumn.setPrefWidth(width * 0.225);
		});

		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> {
	//			employeeSelected = newValue;
				
				if (newValue == null) {
					nameTextField.setText("");
					phoneTextField.setText("");
					rolesComboBox.getSelectionModel().clearSelection();
					emailTextField.setText("");
					passwordTextField.setText(EmployeeController.DEFAULT_PASSWORD);
					saveButton.setText("Registrar");
				} else {
					nameTextField.setText(newValue.getName());
					phoneTextField.setText(newValue.getPhone());
					rolesComboBox.getSelectionModel().select(newValue.getRole());
					emailTextField.setText(newValue.getEmail());
					passwordTextField.setText(newValue.getPassword());
					saveButton.setText("Atualizar");
				}
			});

		MenuItem excluirMenuItem = new MenuItem("Excluir");
		excluirMenuItem.setOnAction(event -> controller.deleteEmployee());
		tableView.setContextMenu(new ContextMenu(excluirMenuItem));

		
		FilteredList<Employee> filteredList = new FilteredList<>(controller.getEmployeesList());
		
		filtedTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(employee -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				if (String.valueOf(employee.getId()).toLowerCase().contains(text)
						|| employee.getName().toLowerCase().contains(text)
						|| employee.getPhone().toLowerCase().contains(text)
						|| employee.getEmail().toLowerCase().contains(text)
						|| employee.getRole().getName().toLowerCase().contains(text)) {
					return true;
				}

				return false;
			});
		});
		SortedList<Employee> sortedList = new SortedList<Employee>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		rolesComboBox.setItems(FXCollections.observableArrayList(Role.values()));

		passwordTextField.setText(EmployeeController.DEFAULT_PASSWORD);

		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Registrar")) {
				controller.addEmployee(
						nameTextField.getText(), phoneTextField.getText(), 
						rolesComboBox.getSelectionModel().getSelectedItem(), 
						emailTextField.getText(), passwordTextField.getText());
			} else {
				controller.updateEmployee(
						nameTextField.getText(), phoneTextField.getText(), 
						rolesComboBox.getSelectionModel().getSelectedItem(), 
						emailTextField.getText(), passwordTextField.getText());
			}
		});
	}

	public BorderPane getRoot() {
		return root;
	}
}
