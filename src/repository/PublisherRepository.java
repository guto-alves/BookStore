package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Publisher;

public class PublisherRepository {
	private PreparedStatement insertNewPublisher;
	private PreparedStatement selectAllPublishers;
	private PreparedStatement updatePublisher;
	private PreparedStatement deletePublisher;
	
	public PublisherRepository() {
		try {
			Connection connection = Database.getConnection();
			insertNewPublisher = connection.prepareStatement(
					"INSERT INTO Publisher " +
					"(Name, Address, Phone) " +
					"VALUES (?, ?, ?)");
			
			updatePublisher = connection.prepareStatement(
					"UPDATE Publisher " +
					"SET Name = ?, Address = ?, Phone = ? " +
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
			insertNewPublisher.setString(2, publisher.getAddress());
			insertNewPublisher.setString(3, publisher.getPhone());
			return insertNewPublisher.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updatePublisher(String name, String address, String phone, 
			String currentName) {
		try {
			updatePublisher.setString(1, name);
			updatePublisher.setString(2, address);
			updatePublisher.setString(3, phone);
			updatePublisher.setString(4, currentName);
			return updatePublisher.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deletePublisher(String name) {
		try {
			deletePublisher.setString(1, name);
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
						resultSet.getString(3)));
			}
			
			return publishers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
}
