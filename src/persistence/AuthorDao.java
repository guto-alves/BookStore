package persistence;

import java.util.List;

import model.Author;

public interface AuthorDao {
	public abstract int addAuthor(Author author);
	
	public abstract int deleteAuthor(Author author);
	
	public abstract int updateAuthor(Author author);
	
	public abstract List<Author> getAllAuthors();
}
