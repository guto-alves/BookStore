package persistence;

import java.util.List;

import model.Sale;

public interface SaleDao {

	public abstract int addSale(Sale sale);

	public abstract int deleteSale(Sale sale);

	public abstract int updateSale(Sale sale);

	public abstract List<Sale> getAllSales();

}
