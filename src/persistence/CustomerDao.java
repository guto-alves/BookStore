package persistence;

import java.util.List;

import model.Customer;

public interface CustomerDao {

	public abstract int addCustomer(Customer customer);
	
	public abstract int updateCustomer(Customer customer, String currentCpf);
	
	public abstract int deleteCustomer(Customer customer);
	
	public abstract List<Customer> getAllCustomers();

}
