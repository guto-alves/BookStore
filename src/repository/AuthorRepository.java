package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Author;

public class AuthorRepository {
	private PreparedStatement insertAuthor;
	private PreparedStatement selectAllAuthors;
	private PreparedStatement updateAuthor;
	private PreparedStatement deleteAuthor;
	
	public AuthorRepository() {
		try {
			Connection connection = Database.getConnection();
			insertAuthor = connection.prepareStatement(
					"INSERT INTO Author " +
					"(FirstName, LastName) " +
					"VALUES (?, ?)");
			
			updateAuthor = connection.prepareStatement(
					"UPDATE Author " +
					"SET FirstName = ?, LastName = ? " +
					"WHERE FirstName = ? AND LastName = ?");
			
			deleteAuthor = connection.prepareStatement(
					"DELETE FROM Author " + 
					"WHERE FirstName = ? AND LastName = ?");
			
			selectAllAuthors = connection.prepareStatement(
					"SELECT * FROM Author");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addAuthor(String firstName, String lastName) {
		try {
			insertAuthor.setString(1, firstName);
			insertAuthor.setString(2, lastName);
			return insertAuthor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateAuthor(String firstName, String lastName, Author author) {
		try {
			updateAuthor.setString(1, firstName);
			updateAuthor.setString(2, lastName);
			updateAuthor.setString(3, author.getFirstName());
			updateAuthor.setString(4, author.getLastName());
			return updateAuthor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteAuthor(Author author) {
		try {
			deleteAuthor.setString(1, author.getFirstName());
			deleteAuthor.setString(2, author.getLastName());
			return deleteAuthor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Author> getAllAuthors() {
		try(ResultSet resultSet = selectAllAuthors.executeQuery()) {
			List<Author> authors = new ArrayList<Author>();
			
			while(resultSet.next()) {
				authors.add(new Author(
						resultSet.getString(1),
						resultSet.getString(2)));
			}
			
			return authors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
}
