package persistence;

import java.util.List;

import model.Sale;

public interface SaleDao {
	
public abstract void add(Sale sale);
	
	public abstract void update(Sale sale);
	
	public abstract void remove(Sale sale);
	
	public abstract List<Sale> selectAll();
	
}
