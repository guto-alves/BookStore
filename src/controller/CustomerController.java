package controller;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Customer;
import repository.CustomerRepository;
import repository.PhoneRepository;

public class CustomerController {
	private Customer customerSelected;
	
	private ObservableList<Customer> customersList;
	private ObservableList<String> phonesList;
	
	private final CustomerRepository customerRepository;
	private final PhoneRepository phoneRepository;
	
	private StringProperty warning = new SimpleStringProperty();

	public CustomerController() {
		customerRepository = new CustomerRepository();
		phoneRepository = new PhoneRepository();
		customersList = FXCollections.observableArrayList();
		phonesList = FXCollections.observableArrayList();
		getAllCustomers();
	}

	private boolean hasInvalidFields(String... fields) {
		for (String string : fields) {
			if (string.isBlank()) {
				return true;
			}
		}
		return false;
	}

	public void addCustomer(String rg, String cpf, String name, 
			String email, List<String> phones, String street, String number,
			String complement, String postalCode) {
		Customer customer = new Customer(rg, cpf, name, email, street, 
				Integer.parseInt(number), complement, postalCode);
	
		int result = customerRepository.addCustomer(customer);
		
		phoneRepository.addPhones(phones, rg);

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Cliente Adicionado",
					"Cliente adicionado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Cliente Não Adicionado",
					"Erro ao adicionar o cliente!");
		}

		getAllCustomers();
//		tableView.getSelectionModel().selectLast();
	}

	public void updateCustomer(String rg, String cpf, String name, 
			String email, List<String> phones, String street, String number,	
			String complement, String postalCode) {
		Customer customer = new Customer(rg, cpf, name, email, street, 
				Integer.parseInt(number), complement, postalCode);
		
		int result = customerRepository.updateCustomer(customer,
				customerSelected.getRg());
	
		phoneRepository.updatePhones(phones, rg);
		
		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Cliente Atualizado",
					"Cliente atualizado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Cliente Não Atualizado",
					"Erro ao atualizar o cliente!");
		}

		getAllCustomers();
	}

	public void deleteCustomer() {
		if (customerSelected == null) {
			return;
		}
		
		int result = customerRepository.deleteCustomer(customerSelected.getRg());

		if (result == 1) {
			displayAlert(AlertType.INFORMATION, "Cliente Excluído",
					"Cliente excluído com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Cliente Não Excluído",
					"Não foi possível excluir o cliente selecionado!");
		}

		customersList.remove(customerSelected);
	}

	private void getAllCustomers() {
		customersList.setAll(customerRepository.getAllCustomers());
	}
	
	public List<String> getPhones() {
		return phoneRepository.getAllPhones(customerSelected.getRg());
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}
	
	public ObservableList<Customer> getCustomersList() {
		return customersList;
	}
	
	public StringProperty getWarning() {
		return warning;
	}
	
	public void setCustomerSelected(Customer customerSelected) {
		this.customerSelected = customerSelected;
	}
	
}
