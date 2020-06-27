package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Author;

public class AuthorBookRepository {
	private PreparedStatement addAuthorBook;
	private PreparedStatement selectAllAuthors;
	private PreparedStatement deleteAuthorBook;
	
	public AuthorBookRepository() {
		try {
			Connection connection = Database.getConnection();
			
			addAuthorBook = connection.prepareStatement(
					"INSERT INTO Author_Book " +
					"(BookISBN, AuthorFirstName, AuthorLastName) " +
					"VALUES (?, ?, ?)");
			
			selectAllAuthors = connection.prepareStatement(
					"SELECT Author.FirstName, Author.LastName " +
					"FROM Book INNER JOIN Author_Book " +
							"ON Book.ISBN = Author_Book.BookISBN " +
					"INNER JOIN Author " + 
							"ON Author_Book.AuthorFirstName = Author.FirstName AND "
							+ "Author_Book.AuthorLastName = Author.LastName " +
					"WHERE Book.ISBN = ?");

			deleteAuthorBook = connection.prepareStatement(
					"DELETE FROM Author_Book " + 
					"WHERE BookISBN = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int add(String isbn, List<Author> authors) {
		try {
			for (Author author : authors) {
				addAuthorBook.setString(1, isbn);
				addAuthorBook.setString(2, author.getFirstName());
				addAuthorBook.setString(3, author.getLastName());
				addAuthorBook.executeUpdate();
			}
			
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int delete(String isbn) {
		try {
			deleteAuthorBook.setString(1, isbn);
			return deleteAuthorBook.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Author> getAllAuthorsOfTheBook(String isbn) {
		try {
			selectAllAuthors.setString(1, isbn);
			ResultSet resultSet = selectAllAuthors.executeQuery();
			List<Author> authors = new ArrayList<>();
			
			while (resultSet.next()) {
				authors.add(new Author(
						resultSet.getString(1), resultSet.getString(2)));
			}
			
			return authors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
