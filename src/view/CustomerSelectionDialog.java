package view;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog; 
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import model.Customer;
import persistence.CustomerDaoImpl;

public class CustomerSelectionDialog {

	public interface CustomerSelectionDialogListener {
		void onDialogClosed(Customer customer);
	}

	private Dialog<ButtonType> dialog;

	private TableView<Customer> tableView = new TableView<>();
	
	private CustomerDaoImpl customerRepository = new CustomerDaoImpl();

	public CustomerSelectionDialog() {
		dialog = new Dialog<>();
		dialog.setTitle("Select a Customer");
		dialog.setResizable(true);

		TextField filterTextField = new TextField();

		tableView = new TableView<>();
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

		ObservableList<Customer> customersList = FXCollections.observableArrayList();
		customersList.setAll(customerRepository.getAllCustomers());
		FilteredList<Customer> filteredList = new FilteredList<>(customersList);
		filterTextField.textProperty()
			.addListener((observable, oldValue, newValue) -> {
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

		ButtonType buttonType = new ButtonType("Select", ButtonData.APPLY);
		dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);
		Node node = dialog.getDialogPane().lookupButton(buttonType);
		node.setDisable(true);
		node.disableProperty().bind(
				tableView.getSelectionModel().selectedItemProperty().isNull());

		FlowPane flowPane = new FlowPane(8, 8, 
				new Label("Filter by"), filterTextField);
		flowPane.setPadding(new Insets(16));
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		dialog.getDialogPane().setContent(borderPane);
	}

	public void show(CustomerSelectionDialogListener listener) {
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isPresent() && result.get() != ButtonType.CANCEL) {
			listener.onDialogClosed(
					tableView.getSelectionModel().getSelectedItem());
		} 
	}

}
