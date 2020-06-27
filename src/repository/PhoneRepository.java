package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneRepository {
	private PreparedStatement insertPhone;
	private PreparedStatement deletePhone;
	private PreparedStatement selectAllPhones;

	public PhoneRepository() {
		try {
			Connection connection = Database.getConnection();

			insertPhone = connection.prepareStatement(
					"INSERT INTO Phone " +
					"(Number, CustomerRG) " + 
					"VALUES (?, ?)");

			deletePhone = connection.prepareStatement(
					"DELETE FROM Phone " + 
					"WHERE CustomerRG = ?");

			selectAllPhones = connection.prepareStatement(
					"SELECT Number " +
					"FROM Phone " +
					"WHERE CustomerRG = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int addPhones(List<String> phones, String rg) {
		try {
			for (String phone : phones) {
				insertPhone.setString(1, phone);
				insertPhone.setString(2, rg);
				insertPhone.addBatch();
			}
			
			insertPhone.executeBatch();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int deletePhones(String rg) {
		try {
			deletePhone.setString(1, rg);
			return deletePhone.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public void updatePhones(List<String> phones, String rg) {
		deletePhones(rg);
		addPhones(phones, rg);
	}

	public List<String> getAllPhones(String rg) {
		try {
			selectAllPhones.setString(1, rg);

			ResultSet phonesResultSet = selectAllPhones.executeQuery();
			List<String> phones = new ArrayList<>();
			
			while (phonesResultSet.next()) {
				phones.add(phonesResultSet.getString(1));
			}

			return phones;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
