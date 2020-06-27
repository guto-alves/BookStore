package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Customer;

public class CustomerRepository {
	private PreparedStatement insertNewCustomer;
	private PreparedStatement updateCustomer;
	private PreparedStatement deleteCustomer;
	private PreparedStatement selectAllCustomers;
	
	public CustomerRepository() {
		try {
			Connection connection = Database.getConnection();
			
			insertNewCustomer = connection.prepareStatement(
					"INSERT INTO Customer " +
					"(RG, CPF, Name, Email, Street, Number, Complement, " + 
					"PostalCode) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			
			updateCustomer = connection.prepareStatement(
					"UPDATE Customer " +
					"SET RG = ?, CPF = ?, Name = ?, Email = ?, Street = ?, " +
					"Number = ?, Complement = ?, PostalCode = ? " +
					"WHERE RG = ?");
			
			deleteCustomer = connection.prepareStatement(
					"DELETE FROM Customer " +
					"WHERE RG = ?");
			
			selectAllCustomers = connection.prepareStatement(
					"SELECT * FROM Customer");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addCustomer(Customer customer) {
		try {
			insertNewCustomer.setString(1, customer.getRg());
			insertNewCustomer.setString(2, customer.getCpf());
			insertNewCustomer.setString(3, customer.getName());
			insertNewCustomer.setString(4, customer.getEmail());
			insertNewCustomer.setString(5, customer.getStreet());
			insertNewCustomer.setInt(6, customer.getNumber());
			insertNewCustomer.setString(7, customer.getComplement());
			insertNewCustomer.setString(8, customer.getPostalCode());
			
			return insertNewCustomer.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateCustomer(Customer customer, String currentRg) {
		try {
			updateCustomer.setString(1, customer.getRg());
			updateCustomer.setString(2, customer.getCpf());
			updateCustomer.setString(3, customer.getName());
			updateCustomer.setString(4, customer.getEmail());
			updateCustomer.setString(5, customer.getStreet());
			updateCustomer.setInt(6, customer.getNumber());
			updateCustomer.setString(7, customer.getComplement());
			updateCustomer.setString(8, customer.getPostalCode());
			updateCustomer.setString(9, currentRg);
			return updateCustomer.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteCustomer(String rg) {
		try {
			deleteCustomer.setString(1, rg);
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
						resultSet.getInt(6),
						resultSet.getString(7),
						resultSet.getString(8)));
			}
			
			return customers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
