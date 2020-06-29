package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Author;

public class AuthorDaoImpl {
	private PreparedStatement insertAuthor;
	private PreparedStatement selectAllAuthors;
	private PreparedStatement updateAuthor;
	private PreparedStatement deleteAuthor;
	
	public AuthorDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			insertAuthor = connection.prepareStatement(
					"INSERT INTO Author " +
					"(FirstName, LastName) " +
					"VALUES (?, ?)");
			
			updateAuthor = connection.prepareStatement(
					"UPDATE Author " +
					"SET FirstName = ?, LastName = ? " +
					"WHERE ID = ?");
			
			deleteAuthor = connection.prepareStatement(
					"DELETE FROM Author " + 
					"WHERE ID = ?");
			
			selectAllAuthors = connection.prepareStatement(
					"SELECT * FROM Author");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addAuthor(Author author) {
		try {
			insertAuthor.setString(1, author.getFirstName());
			insertAuthor.setString(2, author.getLastName());
			return insertAuthor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateAuthor(Author author) {
		try {
			updateAuthor.setString(1, author.getFirstName());
			updateAuthor.setString(2, author.getLastName());
			updateAuthor.setInt(3, author.getId());
			return updateAuthor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteAuthor(Author author) {
		try {
			deleteAuthor.setInt(1, author.getId());
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
						resultSet.getInt("ID"),
						resultSet.getString("FirstName"),
						resultSet.getString("LastName")));
			}
			
			return authors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
