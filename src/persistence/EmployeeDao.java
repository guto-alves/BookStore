package persistence;

import java.util.List;

import model.Employee;

public interface EmployeeDao {
	
	public abstract int addEmployee(Employee employee);
	
	public abstract int updateEmployee(Employee employee);
	
	public abstract int deleteEmployee(int id);
	
	public abstract List<Employee> getAllEmployees();
	
	public abstract Employee findEmployee(String email, String password);

}
