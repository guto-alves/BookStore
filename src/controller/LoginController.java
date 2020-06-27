package controller;

import model.Employee;
import model.EmployeeOn;
import repository.EmployeeRepository;

public class LoginController {
	private EmployeeRepository employeeRepository;
	

	public LoginController() {
		employeeRepository = new EmployeeRepository();
	}
	
	public boolean login(String email, String password) {
		Employee employee = employeeRepository.getEmployee(email, password);
		
		if (employee != null) {
			EmployeeOn.employee = employee;
			return true;
		}
		
		return false;
	}

}
