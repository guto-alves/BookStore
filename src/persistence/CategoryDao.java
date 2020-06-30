package persistence;

import java.util.List;

import model.Category;

public interface CategoryDao {
	
	public abstract int addCategory(Category category);

	public abstract int updateCategory(Category category);

	public abstract int deleteCategory(Category category);

	public abstract List<Category> getAllCategories();
	
}
