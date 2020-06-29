package persistence;

import java.util.List;

import model.Customer;

public interface CustomerDao {

	public abstract void add(Customer customer);
	
	public abstract void update(Customer customer);
	
	public abstract void remove(Customer customer);
	
	public abstract List<Customer> selectAll();

}
