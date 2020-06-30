package persistence;

import java.util.List;

import model.Book;

public interface BookDao {

	public abstract int addBook(Book book);
	
	public abstract int updateBook(Book book, String currentIsbn);
	
	public abstract int deleteBook(String isbn);
	
	public abstract List<Book> getAllBooks();

}
