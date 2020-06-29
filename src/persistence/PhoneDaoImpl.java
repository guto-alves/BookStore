package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoImpl {
	private PreparedStatement insertPhone;
	private PreparedStatement deletePhone;
	private PreparedStatement selectAllPhones;

	public PhoneDaoImpl() {
		try {
			Connection connection = Database.getConnection();

			insertPhone = connection.prepareStatement(
					"INSERT INTO Phone " +
					"(Number, CustomerCPF) " + 
					"VALUES (?, ?)");

			deletePhone = connection.prepareStatement(
					"DELETE FROM Phone " + 
					"WHERE CustomerCPF = ?");

			selectAllPhones = connection.prepareStatement(
					"SELECT Number " +
					"FROM Phone " +
					"WHERE CustomerCPF = ?");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int addPhones(List<String> phones, String cpf) {
		try {
			for (String phone : phones) {
				insertPhone.setString(1, phone);
				insertPhone.setString(2, cpf);
				insertPhone.addBatch();
			}
			
			insertPhone.executeBatch();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int deletePhones(String cpf) {
		try {
			deletePhone.setString(1, cpf);
			return deletePhone.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public void updatePhones(List<String> phones, String cpf) {
		deletePhones(cpf);
		addPhones(phones, cpf);
	}

	public List<String> getAllPhones(String cpf) {
		try {
			selectAllPhones.setString(1, cpf);

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
