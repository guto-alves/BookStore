package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static final String URL = "jdbc:sqlite:Library.db";
	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(URL);
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		return connection;
	}

}
