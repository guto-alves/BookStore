package persistence;

import java.util.List;

import model.Author;

public interface AuthorDao {

	public abstract void add(Author author);
	
	public abstract void update(Author author);
	
	public abstract void remove(Author author);
	
	public abstract List<Author> selectAll();

}
