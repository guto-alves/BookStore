package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;

public class CustomerDaoImpl {
	private PreparedStatement insertNewCustomer;
	private PreparedStatement updateCustomer;
	private PreparedStatement deleteCustomer;
	private PreparedStatement selectAllCustomers;
	
	public CustomerDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			
			insertNewCustomer = connection.prepareStatement(
					"INSERT INTO Customer " +
					"(CPF, Name, Email, Street, Number, Complement, " + 
					"ZipCode) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?)");
			
			updateCustomer = connection.prepareStatement(
					"UPDATE Customer " +
					"SET CPF = ?, Name = ?, Email = ?, Street = ?, " +
					"Number = ?, Complement = ?, ZipCode = ? " +
					"WHERE CPF = ?");
			
			deleteCustomer = connection.prepareStatement(
					"DELETE FROM Customer " +
					"WHERE CPF = ?");
			
			selectAllCustomers = connection.prepareStatement(
					"SELECT * FROM Customer");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addCustomer(Customer customer) {
		try {
			insertNewCustomer.setString(1, customer.getCpf());
			insertNewCustomer.setString(2, customer.getName());
			insertNewCustomer.setString(3, customer.getEmail());
			insertNewCustomer.setString(4, customer.getStreet());
			insertNewCustomer.setString(5, customer.getNumber());
			insertNewCustomer.setString(6, customer.getComplement());
			insertNewCustomer.setString(7, customer.getZipCode());
			
			return insertNewCustomer.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateCustomer(Customer customer, String currentCpf) {
		try {
			updateCustomer.setString(1, customer.getCpf());
			updateCustomer.setString(2, customer.getName());
			updateCustomer.setString(3, customer.getEmail());
			updateCustomer.setString(4, customer.getStreet());
			updateCustomer.setString(5, customer.getNumber());
			updateCustomer.setString(6, customer.getComplement());
			updateCustomer.setString(7, customer.getZipCode());
			updateCustomer.setString(8, currentCpf);
			
			return updateCustomer.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteCustomer(String cpf) {
		try {
			deleteCustomer.setString(1, cpf);
			return deleteCustomer.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Customer> getAllCustomers() {
		try (ResultSet resultSet = selectAllCustomers.executeQuery()) {
			List<Customer> customers = new ArrayList<>();
		
			while (resultSet.next()) {		
				customers.add(new Customer(
						resultSet.getString(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5), 
						resultSet.getString(6),
						resultSet.getString(7)));
			}
			
			return customers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
