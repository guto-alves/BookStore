package persistence;

import java.util.List;

import model.Book;

public interface BookDao {

	public abstract void add(Book employee);
	
	public abstract void update(Book employee);
	
	public abstract void remove(Book employee);
	
	public abstract List<Book> selectAll();

}
