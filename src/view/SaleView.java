package view;

import java.time.LocalDate;
import java.util.stream.Collectors;

import controller.SaleController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos; 
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;
import model.Book;
import model.Sale;

public class SaleView extends BorderPane {
	private TableView<Sale> tableView;
	private TextField filterTextField;

	private DatePicker datePicker;
	private TextField totalTextField;

	private TextField customerCpfTextField;
	private TextField customerNameTextField;
	private Button searchCustomerButton;

	private Button selectBooksButton;
	private ListView<Pair<Book, Integer>> booksListView;

	private TextField employeeNameTextField;

	private Button saveButton;
	private Button cancelButton;

	private SaleController controller;

	public SaleView() {
		controller = new SaleController(); 
		createLayout();
		initialize();
	}

	private void createLayout() {
		tableView = new TableView<>();
		filterTextField = new TextField();
		filterTextField.setPrefWidth(220);
		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filter by"), filterTextField);
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

		customerCpfTextField = new TextField();
		customerCpfTextField.setPromptText("CPF");
		customerCpfTextField.setEditable(false);
		customerNameTextField = new TextField();
		customerNameTextField.setPrefWidth(220);
		customerNameTextField.setPromptText("Nome");
		customerNameTextField.setEditable(false);
		searchCustomerButton = new Button();
		ImageView imageView = new ImageView("search-icon.png");
		imageView.setFitWidth(10);
		imageView.setFitHeight(16);
		imageView.setPreserveRatio(true);
		searchCustomerButton.setGraphic(new ImageView(
				new Image("search-icon.png", 10, 16, true, false)));
		datePicker = new DatePicker();
		datePicker.setEditable(false);
		datePicker.setValue(LocalDate.now());
		totalTextField = new TextField();
		totalTextField.setEditable(false);

		selectBooksButton = new Button("Select Books");
		booksListView = new ListView<>();
		booksListView.setPrefSize(300, 150);
		booksListView.setCellFactory(new Callback<ListView<Pair<Book, Integer>>, ListCell<Pair<Book, Integer>>>() {
			
			@Override
			public ListCell<Pair<Book, Integer>> call(ListView<Pair<Book, Integer>> param) {
				return new ListCell<Pair<Book, Integer>>() {
					
					@Override
					protected void updateItem(Pair<Book, Integer> item, boolean empty) {
						super.updateItem(item, empty);
						
						if (item == null || empty) {
							setGraphic(null);
						} else {
							HBox hbox = new HBox(8);
							hbox.setAlignment(Pos.CENTER_RIGHT);
							
							Label label = new Label(item.getKey().toString());
							label.setMaxWidth(Double.MAX_VALUE);
							HBox.setHgrow(label, Priority.ALWAYS);
							
							Spinner<Integer> spinner = 
									new Spinner<Integer>(1, item.getKey().getCopies(), 
											item.getValue());
							spinner.setPrefWidth(50);
							spinner.valueProperty().addListener((ob, old, newValue) -> {
								double currentTotal = Double.parseDouble(totalTextField.getText());
								
								if (old.doubleValue() < newValue) {
									currentTotal += item.getKey().getPrice();
								} else {
									currentTotal -= item.getKey().getPrice();
								}
				
								totalTextField.setText(String.valueOf(currentTotal));
								
								booksListView.getItems().set(
										booksListView.getItems().indexOf(item), 
										new Pair<Book, Integer>(item.getKey(), newValue));
							});
							
							hbox.getChildren().addAll(label, spinner);
							
							setGraphic(hbox);
						}
						
						setPrefWidth(USE_PREF_SIZE);
					};
				};
			}
		});
		VBox booksVBox = new VBox(5, selectBooksButton, booksListView);
		GridPane.setColumnSpan(booksVBox, GridPane.REMAINING);

		employeeNameTextField = new TextField(); 
		employeeNameTextField.setEditable(false);

		saveButton = new Button("Register");
		cancelButton = new Button("Cancel");

		Label customerLabel = new Label("Customer");
		GridPane.setValignment(customerLabel, VPos.TOP);
		
		gridPane.addRow(0, customerLabel,
				new VBox(new HBox(customerCpfTextField, searchCustomerButton),
						customerNameTextField)); 
		gridPane.addRow(1, new Label("Date"), datePicker);
		gridPane.addRow(2, new Label("Total"), totalTextField);
		gridPane.addRow(3, booksVBox);
		gridPane.addRow(4, new Label("Employee"), employeeNameTextField);
		gridPane.add(new HBox(16, cancelButton, saveButton), 1, 5);
		
		setCenter(new SplitPane(borderPane, gridPane));
	}

	private void initialize() { 
		datePicker.getEditor().textProperty().bindBidirectional(controller.getDate());
		totalTextField.textProperty().bindBidirectional(controller.getTotal());
		customerCpfTextField.textProperty().bindBidirectional(controller.getCustomerCpf());
		customerNameTextField.textProperty().bindBidirectional(controller.getCustomerName());
		employeeNameTextField.textProperty().bindBidirectional(controller.getEmployeeName()); 
		booksListView.setItems(controller.getBooks());

		TableColumn<Sale, String> customerColumn = new TableColumn<>("Customer");
		customerColumn .setCellValueFactory(customer -> 
			new SimpleStringProperty(customer.getValue().getCustomer().getName())
		);
		
		TableColumn<Sale, String> totalColumn = new TableColumn<>("Total");
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		
		TableColumn<Sale, String> saleDateColumn = new TableColumn<>("Date");
		saleDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Sale, String> employeeColumn = new TableColumn<>("Employee");
		employeeColumn.setCellValueFactory(s -> {
			return new SimpleStringProperty(s.getValue().getEmployee().getName());
		}); 
		
		tableView.getColumns().setAll(customerColumn, totalColumn, 
				saleDateColumn, employeeColumn);

		tableView.widthProperty().addListener((observable, oldValue, newValue) -> {
			double width = newValue.doubleValue();
			customerColumn.setPrefWidth(width * 0.30);
			totalColumn.setPrefWidth(width * 0.20); 
			saleDateColumn.setPrefWidth(width * 0.20);
			employeeColumn.setPrefWidth(width * 0.30);
		});

		tableView.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> 
				controller.setSaleSelected(newValue)
			);

		FilteredList<Sale> filteredList = new FilteredList<>(controller.getSales());
		
		filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(sale -> {
				if (newValue == null || newValue.isBlank()) {
					return true;
				}

				String filter = newValue.toLowerCase();

				if (sale.getDate().toLowerCase().contains(filter)
						|| String.valueOf(sale.getTotal()).contains(filter)
						|| sale.getCustomer().getName().toLowerCase().contains(filter)
						|| sale.getCustomer().getCpf().toLowerCase().contains(filter)
						|| sale.getEmployee().getName().toLowerCase().contains(filter)) {
					return true;
				}

				return false;
			});
		});
		SortedList<Sale> sortedList = new SortedList<Sale>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		searchCustomerButton.setOnAction(event -> {
			CustomerSelectionDialog dialog = new CustomerSelectionDialog();
			dialog.show(customer -> {
				customerCpfTextField.setText(customer.getCpf());
				customerNameTextField.setText(customer.getName());
			});
		});

		selectBooksButton.setOnAction(event -> {
			BooksSelectionDialog dialog = new BooksSelectionDialog();
			dialog.show(books -> {
				booksListView.getItems().setAll(
						books.stream()
							.map(book -> new Pair<Book, Integer>(book, 1))
							.collect(Collectors.toList()));
				totalTextField.setText("0.0");
				
				totalTextField.setText(String.valueOf(
						booksListView.getItems()
							.stream()
							.mapToDouble(pair -> pair.getKey().getPrice() * pair.getValue())
							.sum()));
				double discount = 1;
				if (books.size() >= 3) {
					discount = 0.1;
				} else if (books.size() >= 6) {
					discount = 0.15;
				} else if (books.size() >= 10) {
					discount = 0.25;
				}
				double total = Double.parseDouble(totalTextField.getText());
				total = total - (total * discount);
				totalTextField.setText(String.valueOf(total));
			});
		});
		
		MenuItem removeBookMenuItem = new MenuItem("Remove");
		removeBookMenuItem.setOnAction(event -> {
			int index = booksListView.getSelectionModel().getSelectedIndex();
			booksListView.getItems().remove(index);
			totalTextField.setText(String.valueOf(
					booksListView.getItems()
						.stream()
						.mapToDouble(pair -> pair.getKey().getPrice() * pair.getValue())
						.sum()));
		});
		booksListView.setContextMenu(new ContextMenu(removeBookMenuItem));

		cancelButton.setOnAction(event -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveButton.setOnAction(event -> {
			if (saveButton.getText().contains("Register")) {
				controller.addSale();
			} else {
				controller.updateSale(); 
			}
		});
		
		controller.getWarningInfo().addListener((ob, old, newValue) -> 
			AlertUtil.displayAlert(newValue)
		);
	}
}
