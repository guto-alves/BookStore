package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.Book;
import model.Category;
import model.Publisher;

public class SaleBookDaoImpl {
	private PreparedStatement insertNewSaleBook;
	private PreparedStatement selectAllSaleBook;
	private PreparedStatement deleteSaleBook;
	
	public SaleBookDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			
			insertNewSaleBook = connection.prepareStatement(
					"INSERT INTO Sale_Book " +
					"(SaleID, BookISBN, Count) " +
					"VALUES (?, ?, ?)");
			
			deleteSaleBook = connection.prepareStatement(
					"DELETE FROM Sale_Book " +  
					"WHERE SaleID = ?");
			
			selectAllSaleBook = connection.prepareStatement(
					"SELECT * FROM Sale_Book " +
					"INNER JOIN Book " +
							"ON Sale_Book.BookISBN = Book.ISBN " +
					"INNER JOIN Category " +
							"ON Book.CategoryID = Category.ID " +
					"WHERE SaleID = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addSaleBook(int saleId, List<Pair<Book, Integer>> books) { 
		try {
			for (Pair<Book, Integer> book : books) {  
				insertNewSaleBook.setInt(1, saleId);
				insertNewSaleBook.setString(2, book.getKey().getIsbn());
				insertNewSaleBook.setInt(3, book.getValue());
				insertNewSaleBook.addBatch();
			}
			
			insertNewSaleBook.executeBatch();
		
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteSaleBook(int saleId) {
		try {
			deleteSaleBook.setInt(1, saleId);
			
			return deleteSaleBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void updateSaleBook(int saleId, List<Pair<Book, Integer>> books) {
		deleteSaleBook(saleId);
		addSaleBook(saleId, books);
	}
	
	public List<Pair<Book, Integer>> getAllBooks(int saleId) {
		try {
			selectAllSaleBook.setInt(1, saleId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		try(ResultSet resultSet = selectAllSaleBook.executeQuery()) {
			List<Pair<Book, Integer>> books = new ArrayList<Pair<Book, Integer>>();
			
			while(resultSet.next()) { 
				books.add(new Pair<Book, Integer>(
						new Book(
								resultSet.getString("ISBN"),
								resultSet.getString("Title"),
								resultSet.getString("Description"),
								resultSet.getInt("EditionNumber"),
								resultSet.getString("Year"),
								resultSet.getDouble("Price"),
								resultSet.getInt("Copies"),
								new Category(
										resultSet.getInt("CategoryID"),
										resultSet.getString("Name")),
								new Publisher(
										resultSet.getString("PublisherName"),
										null,
										null,
										null,
										null)),
								resultSet.getInt("Count")));
			}
			
			return books;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
