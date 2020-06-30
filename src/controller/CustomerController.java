package controller;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Customer;
import persistence.CustomerDao;
import persistence.DAOFactory;
import persistence.PhoneDao;

public class CustomerController {
	private Customer customerSelected;
	
	private ObservableList<Customer> customersList;
	
	private StringProperty warning = new SimpleStringProperty();
	
	private final CustomerDao customerDao;
	private final PhoneDao phoneDao;

	public CustomerController() {
		customerDao = DAOFactory.getCustomerDao();
		phoneDao = DAOFactory.getPhoneDao();
		
		customersList = FXCollections.observableArrayList();
		
		getAllCustomers();
	}

	public void addCustomer(String cpf, String name, 
			String email, List<String> phones, String street, String number,
			String complement, String zipCode) {
		Customer customer = new Customer(cpf, name, email, street, 
				number, complement, zipCode);
	
		int result = customerDao.addCustomer(customer);

		if (result == 1) {
			phoneDao.addPhones(phones, cpf);
			
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
		
		int result = customerDao.deleteCustomer(customerSelected);

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
