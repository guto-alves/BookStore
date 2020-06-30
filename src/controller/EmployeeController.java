package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Employee;
import model.Role;
import persistence.EmployeeDaoImpl;

public class EmployeeController {
	public static final String DEFAULT_PASSWORD = "12345678";

	private ObservableList<Employee> employeesList; 
	private Employee employeeSelected;
	
	private final EmployeeDaoImpl employeeDao;
	
	private boolean insertionMode; 

	public EmployeeController() {
		employeeDao = new EmployeeDaoImpl();
		employeesList = FXCollections.observableArrayList();
		getAllEmployees();
	}
	
//	private boolean hasInvalidFields() {
//		return nameTextField.getText().isBlank() || 
//				emailTextField.getText().isBlank() ||
//				passwordTextField.getText().isBlank() ||
//				rolesComboBox.getSelectionModel().isEmpty();
//	}
	
	public void onEmployeeSelected(Employee employee) {
		employeeSelected = employee;
	}

	public void addEmployee(String name, String phone, Role role, 
			String email, String password) {
		Employee employee = new Employee(name, phone, role,
				email, password);
		
		int result = employeeDao.addEmployee(employee);

		if (result == 1) {
			getAllEmployees();
			onEmployeeSelected(employee);
			
			displayAlert(AlertType.INFORMATION, "Employee Added", 
					"Employee successfully added.");
		} else {
			displayAlert(AlertType.ERROR, "Employee Not Added",
					"Unable to add employee.");
		}
	}
	
	public void updateEmployee(String name, String phone, Role role, 
			String email, String password) {
		if (employeeSelected == null) {
			System.out.println("Employee null");
			return;
		}
		Employee employee = new Employee(employeeSelected.getId(),
				name, phone, role, email, password);
		
		int result = employeeDao.updateEmployee(employee);

		if (result == 1) {
			getAllEmployees();
			displayAlert(AlertType.INFORMATION, "Employee Updated",
					"Employee successfully updated!");
		} else {
			displayAlert(AlertType.ERROR, "Employee Not Updated",
					"Unable to update employee.");
		}
	}
	
	public void deleteEmployee() {
		if (employeeSelected == null) {
			return;
		}
		
		int result = employeeDao.deleteEmployee(
				employeeSelected.getId());
		
		if (result == 1) {
			employeesList.remove(employeeSelected);
			displayAlert(AlertType.INFORMATION, "Employee Deleted",
					"Employee successfully deleted.");
		} else {
			displayAlert(AlertType.ERROR, "Employee Not Deleted", 
					"Unable to delete employee.");
		}
	}

	private void getAllEmployees() {
		employeesList.setAll(employeeDao.getAllEmployees());
	}
	
	public ObservableList<Employee> getEmployeesList() {
		return employeesList;
	}
	
	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}
	
}
