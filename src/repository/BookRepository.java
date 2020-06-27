package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class BookRepository {
	private PreparedStatement insertBook;
	private PreparedStatement selectAllBooks;
	private PreparedStatement updateBook;
	private PreparedStatement deleteBook;
	
	public BookRepository() {
		try {
			Connection connection = Database.getConnection();
			insertBook = connection.prepareStatement(
					"INSERT INTO Book " +
					"(ISBN, Title, Description, EditionNumber, Year, PublisherName) " +
					"VALUES (?, ?, ?, ?, ?, ?)");
			
			updateBook = connection.prepareStatement(
					"UPDATE Book " +
					"SET ISBN = ?, Title = ?, Description = ?, EditionNumber = ?, " +
					"Year = ?, PublisherName = ? " +
					"WHERE ISBN = ?");
			
			deleteBook = connection.prepareStatement(
					"DELETE FROM Book " + 
					"WHERE ISBN = ?");
			
			selectAllBooks = connection.prepareStatement(
					"SELECT * FROM Book");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addBook(String isbn, String title, String description, 
			String editionNumber, String year, String publisherName) {
		try {
			insertBook.setString(1, isbn);
			insertBook.setString(2, title);
			insertBook.setString(3, description);
			insertBook.setString(4, editionNumber);
			insertBook.setString(5, year);
			insertBook.setString(6, publisherName);
			return insertBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateBook(String isbn, String title, String description, 
			String editionNumber, String year, String publisherName, String currentIsbn) {
		try {
			updateBook.setString(1, isbn);
			updateBook.setString(2, title);
			updateBook.setString(3, description);
			updateBook.setString(4, editionNumber);
			updateBook.setString(5, year);
			updateBook.setString(6, publisherName);
			updateBook.setString(7, currentIsbn);
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
		}
		
		return null ;
	}

}
