package persistence;

import java.util.List;

import model.Category;

public interface CategoryDao {

	public abstract void add(Category category);

	public abstract void update(Category category);

	public abstract void remove(Category category);

	public abstract List<Category> selectAll();

}
