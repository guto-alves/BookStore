package controller;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Customer;
import persistence.CustomerDaoImpl;
import persistence.PhoneDaoImpl;

public class CustomerController {
	private Customer customerSelected;
	
	private ObservableList<Customer> customersList;
	
	private final CustomerDaoImpl customerDao;
	private final PhoneDaoImpl phoneDao;
	
	private StringProperty warning = new SimpleStringProperty();

	public CustomerController() {
		customerDao = new CustomerDaoImpl();
		phoneDao = new PhoneDaoImpl();
		
		customersList = FXCollections.observableArrayList();
		
		getAllCustomers();
	}

//	private boolean hasInvalidFields(String... fields) {
//		for (String string : fields) {
//			if (string.isBlank()) {
//				return true;
//			}
//		}
//		
//		return false;
//	}

	public void addCustomer(String cpf, String name, 
			String email, List<String> phones, String street, String number,
			String complement, String zipCode) {
		Customer customer = new Customer(cpf, name, email, street, 
				number, complement, zipCode);
	
		int result = customerDao.addCustomer(customer);
		
		phoneDao.addPhones(phones, cpf);

		if (result == 1) {
			getAllCustomers();
			displayAlert(AlertType.INFORMATION, "Customer Added",
					"Customer successfully added!");
		} else {
			displayAlert(AlertType.ERROR, "Customer Not Added",
					"Unable to add customer.");
		}	
	}

	public void updateCustomer(String cpf, String name, 
			String email, List<String> phones, String street, String number,	
			String complement, String zipCode) {
		Customer customer = new Customer(cpf, name, email, street, 
				number, complement, zipCode);
		
		int result = customerDao.updateCustomer(customer,
				customerSelected.getCpf());
	
		phoneDao.updatePhones(phones, cpf);
		
		if (result == 1) {
			getAllCustomers();
			
			displayAlert(AlertType.INFORMATION, "Customer Updated",
					"Customer successfully updated.");
		} else {
			displayAlert(AlertType.ERROR, "Customer Not Updated",
					"Unable to update customer.");
		}
	}

	public void deleteCustomer() {
		if (customerSelected == null) {
			return;
		}
		
		phoneDao.deletePhones(customerSelected.getCpf());
		
		int result = customerDao.deleteCustomer(
				customerSelected.getCpf());

		if (result == 1) {
			customersList.remove(customerSelected);
			
			displayAlert(AlertType.INFORMATION, "Customer Deleted",
					"Customer successfully deleted.");
		} else {
			displayAlert(AlertType.ERROR, "Customer Not Deleted",
					"Unable to delete customer!");
		}
	}

	private void getAllCustomers() {
		customersList.setAll(customerDao.getAllCustomers());
	}
	
	public List<String> getAllPhones() {
		return phoneDao.getAllPhones(
				customerSelected.getCpf());
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText("Message");
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
