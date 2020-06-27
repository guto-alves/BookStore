package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryRepository {
	private PreparedStatement insertCategory;
	private PreparedStatement selectAllCategories;
	private PreparedStatement updateCategory;
	private PreparedStatement deleteCategory;
	
	public CategoryRepository() {
		try {
			Connection connection = Database.getConnection();
			insertCategory = connection.prepareStatement(
					"INSERT INTO Category " +
					"(Name) " +
					"VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			updateCategory = connection.prepareStatement(
					"UPDATE Category " +
					"SET Name = ? " +
					"WHERE ID = ?");
			
			deleteCategory = connection.prepareStatement(
					"DELETE FROM Category " + 
					"WHERE ID = ?");
			
			selectAllCategories = connection.prepareStatement(
					"SELECT * FROM Category");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public int addCategory(Category category) {
		try {
			insertCategory.setString(1, category.getName());
			
			insertCategory.executeUpdate();
			
			ResultSet keysResultSet = insertCategory.getGeneratedKeys();
			
			keysResultSet.next();
			
			return keysResultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateCategory(Category category, int id) {
		try {
			updateCategory.setString(1, category.getName());
			updateCategory.setInt(2, id);
			return updateCategory.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteCategory(String id) {
		try {
			deleteCategory.setString(1, id);
			return deleteCategory.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Category> getAllCategories() {
		try(ResultSet resultSet = selectAllCategories.executeQuery()) {
			List<Category> categories = new ArrayList<Category>();
			
			while(resultSet.next()) {
				categories.add(new Category(
						resultSet.getInt(1),
						resultSet.getString(2)));
			}
			
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
