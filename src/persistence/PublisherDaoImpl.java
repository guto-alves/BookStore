package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Publisher;

public class PublisherDaoImpl implements PublisherDao {
	private PreparedStatement insertNewPublisher;
	private PreparedStatement selectAllPublishers;
	private PreparedStatement updatePublisher;
	private PreparedStatement deletePublisher;
	
	public PublisherDaoImpl() {
		try {
			Connection connection = Database.getConnection();
			insertNewPublisher = connection.prepareStatement(
					"INSERT INTO Publisher " +
					"(Name, Phone, Street, Number, Complement) " +
					"VALUES (?, ?, ?, ?, ?)");
			
			updatePublisher = connection.prepareStatement(
					"UPDATE Publisher " +
					"SET Name = ?, Phone = ?, Street = ? , Number = ?, " +
					"Complement = ? " +
					"WHERE Name = ?");
			
			deletePublisher = connection.prepareStatement(
					"DELETE FROM Publisher " + 
					"WHERE Name = ?");
			
			selectAllPublishers = connection.prepareStatement(
					"SELECT * FROM Publisher");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addPublisher(Publisher publisher) {
		try {
			insertNewPublisher.setString(1, publisher.getName());
			insertNewPublisher.setString(2, publisher.getPhone());
			insertNewPublisher.setString(3, publisher.getStreet());
			insertNewPublisher.setString(4, publisher.getNumber());
			insertNewPublisher.setString(5, publisher.getComplement());
			
			return insertNewPublisher.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updatePublisher(Publisher publisher, String currentName) {
		try {
			updatePublisher.setString(1, publisher.getName());
			updatePublisher.setString(2, publisher.getPhone());
			updatePublisher.setString(3, publisher.getStreet());
			updatePublisher.setString(4, publisher.getNumber());
			updatePublisher.setString(5, publisher.getComplement());
			updatePublisher.setString(6, currentName);
			
			return updatePublisher.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deletePublisher(Publisher publisher) {
		try {
			deletePublisher.setString(1, publisher.getName());
			
			return deletePublisher.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Publisher> getAllPublishers() {
		try(ResultSet resultSet = selectAllPublishers.executeQuery()) {
			List<Publisher> publishers = new ArrayList<Publisher>();
			
			while(resultSet.next()) {
				publishers.add(new Publisher(
						resultSet.getString(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5)));
			}
			
			return publishers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
}
