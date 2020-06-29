package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Employee;
import model.Sale;

public class SaleDaoImpl {
	private PreparedStatement insertNewSale;
	private PreparedStatement updateSale;
	private PreparedStatement deleteSale;
	private PreparedStatement selectAllSales;

	private SaleBookDaoImpl bookDaoImpl;

	public SaleDaoImpl() {
		try {
			bookDaoImpl = new SaleBookDaoImpl();
			
			Connection connection = Database.getConnection();
			
			insertNewSale = connection.prepareStatement(
					"INSERT INTO Sale " +
					"(Date,  Total, CustomerCPF, EmployeeID) " +
					"VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			updateSale = connection.prepareStatement(
					"UPDATE Sale " +
					"SET Date = ?, Total = ?, CustomerCPF = ?, EmployeeID = ? " +
					"WHERE ID = ?");
			
			deleteSale = connection.prepareStatement(
					"DELETE FROM Sale " + 
					"WHERE ID = ?");

			selectAllSales = connection.prepareStatement(
					"SELECT Sale.ID, Sale.Date, Sale.Total,  " +
					"Customer.CPF, Customer.Name, Customer.Email, " +
					"Sale.EmployeeID, Employee.Name AS EmployeeName " +
					"FROM Sale INNER JOIN Customer " +
							"ON Sale.CustomerCPF = Customer.CPF " +
					"INNER JOIN Employee " +
							"ON Sale.EmployeeID = Employee.ID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int addSale(Sale sale) {
		try {
			insertNewSale.setString(1, sale.getDate());
			insertNewSale.setDouble(2, sale.getTotal());
			insertNewSale.setString(3, sale.getCustomer().getCpf());
			insertNewSale.setInt(4, sale.getEmployee().getId());

			insertNewSale.executeUpdate();
			
			ResultSet resultSet = insertNewSale.getGeneratedKeys();
			resultSet.next();
			
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int updateSale(Sale sale) {
		try {
			updateSale.setString(1, sale.getDate());
			updateSale.setDouble(2, sale.getTotal());
			updateSale.setString(3, sale.getCustomer().getCpf());
			updateSale.setInt(4, sale.getEmployee().getId());
			updateSale.setInt(5, sale.getId());

			return updateSale.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public int deleteSale(Sale sale) {
		try {
			deleteSale.setInt(1, sale.getId());

			return deleteSale.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public List<Sale> getAllSales() {
		try (ResultSet resultSet = selectAllSales.executeQuery()) {
			List<Sale> sales = new ArrayList<>();

			while (resultSet.next()) {
				Customer customer = new Customer();
				customer.setCpf(resultSet.getString("CPF"));
				customer.setName(resultSet.getString("Name"));
				
				Employee employee = new Employee();
				employee.setId(resultSet.getInt("EmployeeID"));
				employee.setName(resultSet.getString("EmployeeName"));
				
				Sale sale = new Sale(
						resultSet.getInt("ID"), 
						resultSet.getString("Date"), 
						resultSet.getDouble("Total"),
						customer,
						employee);

				sales.add(sale);
			}

			return sales;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
