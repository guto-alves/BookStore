package persistence;

import java.util.List;

import javafx.util.Pair;
import model.Book;

public interface SaleBookDao {

	public abstract int addSaleBook(int saleId, List<Pair<Book, Integer>> books);

	public abstract int deleteSaleBook(int saleId);
	
	public abstract void updateSaleBook(int saleId, List<Pair<Book, Integer>> books);

	public abstract List<Pair<Book, Integer>> getAllBooks(int saleId);

}
