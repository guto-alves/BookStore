package view;

import java.time.LocalDate;

import controller.LoanController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Book;
import model.Loan;

public class LoanView {
	private BorderPane root;

	private TableView<Loan> tableView;
	private TextField filtedTextField;

	private DatePicker loanDatePicker;
	private DatePicker returnDatePicker;
	private DatePicker dateReturnedDatePicker;

	private TextField customerRgTextField;
	private TextField customerNameTextField;
	private Button searchCustomerButton;

	private Button selectBooksButton;
	private ListView<Book> booksListView;

	private TextField employeeNameTextField;

	private Button saveButton;
	private Button cancelButton;

	private final LoanController controller;

	public LoanView() {
		controller = new LoanController();
		createLayout();
		addListeners();
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

		customerRgTextField = new TextField();
		customerRgTextField.setPromptText("RG");
		customerRgTextField.setEditable(false);
		customerNameTextField = new TextField();
		customerNameTextField.setPromptText("Nome");
		customerNameTextField.setEditable(false);
		searchCustomerButton = new Button();
		ImageView imageView = new ImageView("search-icon.png");
		imageView.setFitWidth(10);
		imageView.setFitHeight(16);
		imageView.setPreserveRatio(true);
		searchCustomerButton.setGraphic(new ImageView(
				new Image("search-icon.png", 10, 16, true, false)));
		loanDatePicker = new DatePicker();
		loanDatePicker.setValue(LocalDate.now());
		loanDatePicker.setEditable(false);
		returnDatePicker = new DatePicker();
		returnDatePicker.setEditable(false);
		dateReturnedDatePicker = new DatePicker();
		dateReturnedDatePicker.setEditable(false);

		selectBooksButton = new Button("Selecionar Livros");
		booksListView = new ListView<>();
		booksListView.setPrefHeight(60);
		VBox booksVBox = new VBox(8, selectBooksButton, booksListView);
		GridPane.setColumnSpan(booksVBox, GridPane.REMAINING);

		employeeNameTextField = new TextField(); 
		employeeNameTextField.setEditable(false);

		saveButton = new Button("Registrar");
		cancelButton = new Button("Cancelar");

		Label customerLabel = new Label("Cliente");
		GridPane.setValignment(customerLabel, VPos.TOP);
		gridPane.addRow(0, customerLabel,
				new VBox(
						new HBox(customerRgTextField, searchCustomerButton),
						customerNameTextField)); 
		gridPane.addRow(1, new Label("Data do Empréstimo"), loanDatePicker);
		gridPane.addRow(2, new Label("Data Limite"), returnDatePicker);
		gridPane.addRow(3, new Label("Data da Devolução"), dateReturnedDatePicker);
		gridPane.addRow(4, booksVBox);
		gridPane.addRow(5, new Label("Funcionário"), employeeNameTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 6);

		root.setCenter(new SplitPane(borderPane, gridPane));
	}

	private void addListeners() {
		loanDatePicker.getEditor().textProperty().bindBidirectional(controller.getLoanDate());
		returnDatePicker.getEditor().textProperty().bindBidirectional(controller.getReturnDate());
		dateReturnedDatePicker.getEditor().textProperty().bindBidirectional(controller.getDateReturned());
		customerRgTextField.textProperty().bindBidirectional(controller.getCustomerRg());
		customerNameTextField.textProperty().bindBidirectional(controller.getCustomerName());
		employeeNameTextField.textProperty().bind(controller.getEmployeeName()); 
		booksListView.setItems(controller.getBooks());

		TableColumn<Loan, String> customerColumn = new TableColumn<>("Cliente");
		customerColumn .setCellValueFactory(customer -> 
			new SimpleStringProperty(customer.getValue().getCustomer().getName())
		);
		
		TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Data Empréstimo");
		loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));

		TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Data Retorno");
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

		TableColumn<Loan, String> dateReturnedColumn = new TableColumn<>("Data Retornado");
		dateReturnedColumn.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));
		
		tableView.getColumns().setAll(customerColumn, loanDateColumn, 
				returnDateColumn, dateReturnedColumn);

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			customerColumn.setPrefWidth(width * 0.25);
			loanDateColumn.setPrefWidth(width * 0.25);
			returnDateColumn.setPrefWidth(width * 0.25);
			dateReturnedColumn.setPrefWidth(width * 0.25);
		});

		controller.getLoanSelected().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				tableView.getSelectionModel().clearSelection();
			}
		});

		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			controller.setLoanSelected(newValue);

			if (newValue == null) {
				loanDatePicker.getEditor().setText("");
				returnDatePicker.getEditor().setText("");
				dateReturnedDatePicker.getEditor().setText("");
				customerNameTextField.setText("");
				customerRgTextField.setText("");
				booksListView.getItems().clear();
				saveButton.setText("Registrar");
			} else {
				loanDatePicker.getEditor().textProperty().bindBidirectional(controller.getLoanDate());
				returnDatePicker.getEditor().setText(newValue.getReturnDate());
				dateReturnedDatePicker.getEditor().setText(newValue.getDateReturned());
				customerNameTextField.setText(newValue.getCustomer().getName());
				customerRgTextField.setText(newValue.getCustomer().getRg());
				booksListView.getItems().setAll(newValue.getBooks());
				saveButton.setText("Atualizar");
			}
		});

		FilteredList<Loan> filteredList = new FilteredList<>(controller.getLoans());
		
		filtedTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(loan -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String text = newValue.toLowerCase();

				if (loan.getLoanDate().toLowerCase().contains(text)
						|| loan.getReturnDate().toLowerCase().contains(text)
						|| loan.getDateReturned().toLowerCase().contains(text)
						|| loan.getCustomer().getName().toLowerCase().contains(text)
						|| loan.getCustomer().getRg().toLowerCase().contains(text)) {
					return true;
				}

				return false;
			});
		});
		SortedList<Loan> sortedList = new SortedList<Loan>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		searchCustomerButton.setOnAction(event -> {
			CustomerSelectionDialog dialog = new CustomerSelectionDialog();
			dialog.show(customer -> {
				customerRgTextField.setText(customer.getRg());
				customerNameTextField.setText(customer.getName());
			});
		});

		selectBooksButton.setOnAction(event -> {
			BooksSelectionDialog dialog = new BooksSelectionDialog();
			dialog.show(books -> {
				booksListView.getItems().setAll(books);
			});
		});

		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Registrar")) {
				controller.addLoan();
			} else {
				controller.updateLoan();
			}
		});
		
		controller.getWarningInfo().addListener((ob, old, newValue) -> 
			AlertUtil.displayAlert(newValue)
		);
	}

	public BorderPane getRoot() {
		return root;
	}

}
