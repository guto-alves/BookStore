package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryDaoImpl {
	private PreparedStatement insertCategory;
	private PreparedStatement selectAllCategories;
	private PreparedStatement updateCategory;
	private PreparedStatement deleteCategory;
	
	public CategoryDaoImpl() {
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
		
			return insertCategory.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateCategory(Category category) {
		try {
			updateCategory.setString(1, category.getName());
			updateCategory.setInt(2, category.getId());
			return updateCategory.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int deleteCategory(Category category) {
		try {
			deleteCategory.setInt(1, category.getId());
			
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
