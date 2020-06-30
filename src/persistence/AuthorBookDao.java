package persistence;

import java.util.List;

import model.Author;

public interface AuthorBookDao {
	public abstract int add(List<Author> authors, String isbn);

	public abstract int delete(String isbn);

	public abstract void update(List<Author> authors, String isbn);

	public abstract List<Author> getAllAuthors(String isbn);
}
