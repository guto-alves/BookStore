package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.Category;
import model.Publisher;

public class BookDaoImpl implements BookDao {
	private PreparedStatement insertBook;
	private PreparedStatement updateBook;
	private PreparedStatement deleteBook;
	private PreparedStatement selectAllBooks;
	
	public BookDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			
			insertBook = connection.prepareStatement(
					"INSERT INTO Book " +
					"(ISBN, Title, Description, EditionNumber, Year, Price, " + 
					"Copies, PublisherName, CategoryID) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			updateBook = connection.prepareStatement(
					"UPDATE Book " +
					"SET ISBN = ?, Title = ?, Description = ?, EditionNumber = ?, " +
					"Year = ?, Price = ?, Copies = ?, PublisherName = ?, CategoryID = ? " +
					"WHERE ISBN = ?");
			
			deleteBook = connection.prepareStatement(
					"DELETE FROM Book " + 
					"WHERE ISBN = ?");
			
			selectAllBooks = connection.prepareStatement(
					"SELECT * FROM " +
					"Book INNER JOIN Publisher " + 
							"ON Book.PublisherName = Publisher.Name " +
					"INNER JOIN Category " +
							"ON Book.CategoryID = Category.ID");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addBook(Book book) {
		try {
			insertBook.setString(1, book.getIsbn());
			insertBook.setString(2, book.getTitle());
			insertBook.setString(3, book.getDescription());
			insertBook.setInt(4, book.getEditionNumber());
			insertBook.setString(5, book.getYear());
			insertBook.setDouble(6, book.getPrice());
			insertBook.setInt(7, book.getCopies());
			insertBook.setString(8, book.getPublisher().getName());
			insertBook.setInt(9, book.getCategory().getId());
			
			return insertBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateBook(Book book, String currentIsbn) {
		try {
			updateBook.setString(1, book.getIsbn());
			updateBook.setString(2, book.getTitle());
			updateBook.setString(3, book.getDescription());
			updateBook.setInt(4, book.getEditionNumber());
			updateBook.setString(5, book.getYear());
			updateBook.setDouble(6, book.getPrice());
			updateBook.setInt(7, book.getCopies());
			updateBook.setString(8, book.getPublisher().getName());
			updateBook.setInt(9, book.getCategory().getId());
			updateBook.setString(10, currentIsbn);
			
			return updateBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteBook(String isbn) {
		try {
			deleteBook.setString(1, isbn);
			return deleteBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Book> getAllBooks() { 
		try(ResultSet resultSet = selectAllBooks.executeQuery()) {
			List<Book> books = new ArrayList<Book>();
			
			while(resultSet.next()) {
				books.add(new Book(
						resultSet.getString("ISBN"),
						resultSet.getString("Title"),
						resultSet.getString("Description"),
						resultSet.getInt("EditionNumber"),
						resultSet.getString("Year"),
						resultSet.getDouble("Price"),
						resultSet.getInt("Copies"),
						new Category(
								resultSet.getInt("ID"),
								resultSet.getString("Name")),
						new Publisher(
								resultSet.getString("PublisherName"),
								resultSet.getString("Phone"),
								resultSet.getString("Street"),
								resultSet.getString("Number"), 
								resultSet.getString("Complement"))));
			}
			
			return books;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null ;
	}

}
