package controller;

import model.Employee;
import model.EmployeeOn;
import persistence.DAOFactory;
import persistence.EmployeeDao;

public class LoginController {
	private EmployeeDao employeeRepository;
	
	public LoginController() {
		employeeRepository = DAOFactory.getEmployeeDao();
	}

	public boolean login(String email, String password) {
		Employee employee = employeeRepository.findEmployee(email, password);

		if (employee != null) {
			EmployeeOn.employee = employee;
			return true;
		}

		return false;
	}
}
