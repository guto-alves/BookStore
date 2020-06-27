package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.Customer;
import model.Employee;
import model.Loan;
import model.Role;

public class LoanRepository {
	private PreparedStatement insertNewLoan;
	private PreparedStatement selectAllLoans;
	private PreparedStatement updateLoan;
	private PreparedStatement selectAllBook;
	
	public LoanRepository() { 
		try {
			Connection connection = Database.getConnection();
			
			insertNewLoan = connection.prepareStatement(
					"INSERT INTO Loan " +
					"(LoanDate, ReturnDate, CustomerRG, BookISBN, EmployeeId) " + 
					"VALUES (?, ?, ?, ?, ?)");
			
			updateLoan = connection.prepareStatement(
					"UPDATE Loan " + 
					"SET LoanDate = ?, ReturnDate = ?, DateReturned = ?, " + 
					"CustomerRG = ?, BookISBN = ?, EmployeeId = ? " + 
					"WHERE LoanDate = ? AND CustomerRG = ? AND BookISBN = ?");
			
			selectAllLoans = connection.prepareStatement(
					"SELECT DISTINCT LoanDate, ReturnDate, DateReturned, " + 
					"Customer.RG, Customer.CPF, Customer.Name, Customer.Email, " +
					"Customer.Street, Customer.Number, Customer.Complement, " +
					"Customer.PostalCode, Employee.ID, Employee.Name, Employee.Phone, " +
					"Employee.Email, Employee.Password, Employee.Role " +
					"FROM Customer INNER JOIN Loan "+
						"ON Customer.RG = Loan.CustomerRG " +
					"INNER JOIN Employee " +
						"ON Loan.EmployeeID = Employee.ID");
			
			selectAllBook = connection.prepareStatement(
					"SELECT \r\n" + 
					"    Book.ISBN,\r\n" + 
					"    Book.Title,\r\n" + 
					"    Book.Description,\r\n" + 
					"    Book.EditionNumber,\r\n" + 
					"    Book.Year,\r\n" + 
					"    Category.ID,\r\n" + 
					"    Category.Name\r\n" + 
					"FROM\r\n" + 
					"    Loan INNER JOIN Book\r\n" + 
					"        ON Loan.BookISBN = Book.ISBN\r\n" + 
					"    INNER JOIN Category\r\n" + 
					"        ON Book.CategoryID = Category.ID\r\n" + 
					"WHERE\r\n" + 
					"    Loan.LoanDate = ? AND Loan.CustomerRG = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addLoan(Loan loan) {
		try {
			for (Book book : loan.getBooks()) {
				insertNewLoan.setString(1, loan.getLoanDate());
				insertNewLoan.setString(2, loan.getReturnDate());
				insertNewLoan.setString(3, loan.getCustomer().getRg());
				insertNewLoan.setString(4, book.getIsbn());
				insertNewLoan.setInt(5, loan.getEmployee().getId());
				insertNewLoan.addBatch();
			}
			
			insertNewLoan.executeBatch();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateLoan(String loanDate, String returnDate, String returnDateed,
			String customerRg, String employeeId, List<Book> isbns, Loan currentLoan) {
		try {
			for (Book isbn : isbns) {
				updateLoan.setString(1, loanDate);
				updateLoan.setString(2, returnDate);
				updateLoan.setString(3, returnDateed);
				updateLoan.setString(4, customerRg);
				updateLoan.setString(5, isbn.getIsbn());
				updateLoan.setString(6, employeeId);
				updateLoan.setString(7, currentLoan.getLoanDate());
				updateLoan.addBatch();
			}
			
			updateLoan.executeBatch();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Loan> getAllLoans() {
		try (ResultSet resultSet = selectAllLoans.executeQuery()) {
			List<Loan> loans = new ArrayList<>();
			
			while (resultSet.next()) {				
				Loan loan = new Loan(
						resultSet.getString(1), 
						resultSet.getString(2),
						resultSet.getString(3), 
						new Customer(
								resultSet.getString(4),
								resultSet.getString(5), 
								resultSet.getString(6), 
								resultSet.getString(7),
								resultSet.getString(8), 
								resultSet.getInt(9),
								resultSet.getString(10),
								resultSet.getString(11)),
						new Employee(
								resultSet.getInt(12),
								resultSet.getString(13), 
								resultSet.getString(14), 
								Role.valueOf(resultSet.getString(15)),
								resultSet.getString(16),
								resultSet.getString(17)));
				
				loan.setBooks(getAllBooks(
						loan.getLoanDate(), loan.getCustomer().getRg()));
				
				loans.add(loan);
			}
			
			return loans;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private List<Book> getAllBooks(String loanDate, String rg) {
		try {
			selectAllBook.setString(1, loanDate);
			selectAllBook.setString(2, rg);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		try (ResultSet resultSet = selectAllBook.executeQuery()){
			List<Book> books = new ArrayList<>();
			
			while (resultSet.next()) {
				books.add(new Book(
						resultSet.getString(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getInt(4), 
						resultSet.getInt(5),
						resultSet.getString(6)));
			}
			
			return books;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
