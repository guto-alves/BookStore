package controller;

import model.Employee;
import model.EmployeeOn;
import persistence.EmployeeDaoImpl;

public class LoginController {
	private EmployeeDaoImpl employeeRepository;
	

	public LoginController() {
		employeeRepository = new EmployeeDaoImpl();
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
