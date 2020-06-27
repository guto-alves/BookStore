package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Employee;
import model.Role;
import repository.EmployeeRepository;

public class EmployeeController {
	public static final String DEFAULT_PASSWORD = "12345678";

	private ObservableList<Employee> employeesList;
	private Employee employeeSelected;
	
	private final EmployeeRepository employeeRepository;
	
	private boolean insertionMode; 

	public EmployeeController() {
		employeeRepository = new EmployeeRepository();
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
		
	}

	public void addEmployee(String name, String phone, Role role, 
			String email, String password) {
		Employee employee = new Employee(name, phone, role,
				email, password);
		
		int result = employeeRepository.addEmployee(employee);

		if (result == 1) {
			getAllEmployees();
			onEmployeeSelected(employee);
			
			displayAlert(AlertType.INFORMATION, "Funcionário Adicionado", 
					"Funcionário adicionado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Funcionário Não Adicionado",
					"Erro ao adicionar o funcionário!");
		}
	}
	
	public void updateEmployee(String name, String phone, Role role, 
			String email, String password) {
		Employee employee = new Employee(employeeSelected.getId(),
				name, phone, role, email, password);
		
		int result = employeeRepository.updateEmployee(employee);

		if (result == 1) {
			getAllEmployees();
			displayAlert(AlertType.INFORMATION, "Funcionário Atualizado",
					"Funcionário atualizado com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Funcionário Não Atualizado",
					"Erro ao atualizar o funcionário!");
		}
	}
	
	public void deleteEmployee() {
		if (employeeSelected == null) {
			return;
		}
		
		int result = employeeRepository.deleteEmployee(
				employeeSelected.getId());
		
		if (result == 1) {
			employeesList.remove(employeeSelected);
			displayAlert(AlertType.INFORMATION, "Funcionário Excluído",
					"Funcionário excluído com sucesso!");
		} else {
			displayAlert(AlertType.ERROR, "Funcionário Não Excluído", 
					"Não foi possível excluir a funcionário selecionado!");
		}
	}

	private void getAllEmployees() {
		employeesList.setAll(employeeRepository.getAllEmployees());
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
