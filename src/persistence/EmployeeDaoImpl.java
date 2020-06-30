package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.Role;

public class EmployeeDaoImpl implements EmployeeDao { 
	private PreparedStatement insertEmployee;
	private PreparedStatement updateEmployee;
	private PreparedStatement deleteEmployee;
	private PreparedStatement selectAllEmployees;
	private PreparedStatement selectEmployeeByEmailAndPassword;
	
	public EmployeeDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			
			insertEmployee = connection.prepareStatement(
					"INSERT INTO Employee " +
					"(Name, Phone, Role, Email, Password) " +
					"VALUES (?, ?, ?, ?, ?)");
			
			updateEmployee = connection.prepareStatement(
					"UPDATE Employee " +
					"SET Name = ?, Phone = ?, Role = ?, Email = ?, " + 
					"Password = ? " +
					"WHERE ID = ?");
			
			deleteEmployee = connection.prepareStatement(
					"DELETE FROM Employee " + 
					"WHERE ID = ?");
			
			selectAllEmployees = connection.prepareStatement(
					"SELECT * FROM Employee");
			
			selectEmployeeByEmailAndPassword = connection.prepareStatement(
					"SELECT * FROM  Employee " +
					"WHERE Email = ? AND Password = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addEmployee(Employee employee) {
		try {
			insertEmployee.setString(1, employee.getName());
			insertEmployee.setString(2, employee.getPhone());
			insertEmployee.setString(3, employee.getRole().name());
			insertEmployee.setString(4, employee.getEmail());
			insertEmployee.setString(5, employee.getPassword());
			
			return insertEmployee.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateEmployee(Employee employee) {
		try {
			updateEmployee.setString(1, employee.getName());
			updateEmployee.setString(2, employee.getPhone());
			updateEmployee.setString(3, employee.getRole().name());
			updateEmployee.setString(4, employee.getEmail());
			updateEmployee.setString(5, employee.getPassword());
			updateEmployee.setInt(6, employee.getId());
			
			return updateEmployee.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteEmployee(int id) { 
		try {
			deleteEmployee.setInt(1, id);
			return deleteEmployee.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	 
	public List<Employee> getAllEmployees() {
		try(ResultSet resultSet = selectAllEmployees.executeQuery()) {
			List<Employee> employees = new ArrayList<>();
			
			while(resultSet.next()) {
				employees.add(new Employee(
						resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getString(3),
						Role.valueOf(resultSet.getString(4)),
						resultSet.getString(5),
						resultSet.getString(6)));
			}
			
			return employees;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Employee findEmployee(String email, String password) {
		try {
			selectEmployeeByEmailAndPassword.setString(1, email);
			selectEmployeeByEmailAndPassword.setString(2, password);
			
			ResultSet resultSet = selectEmployeeByEmailAndPassword
					.executeQuery();
			
			Employee employee = null;
			
			if (resultSet.next()) {
				employee = new Employee(
						resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getString(3),
						Role.valueOf(resultSet.getString(4)),
						resultSet.getString(5),
						resultSet.getString(6));
			}
			
			return employee;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
