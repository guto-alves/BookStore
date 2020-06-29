package persistence;

import java.util.List;

import model.Employee;

public interface EmployeeDao {
	
	public abstract void add(Employee employee);
	
	public abstract void update(Employee employee);
	
	public abstract void remove(Employee employee);
	
	public abstract List<Employee> selectAll();

}
