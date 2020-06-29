package model;

public class Sale {
	private int id;
	private String date;
	private double total;
	private Customer customer;
	private Employee employee;

	public Sale() {
	}
	
	public Sale(String date, double total, Customer customer, 
			Employee employee) {
		this(0, date, total, customer, employee);
	}
	
	public Sale(int id, String date, double total, Customer customer, 
			Employee employee) {
		this.id = id;
		this.date = date;
		this.total = total;
		this.customer = customer;
		this.employee = employee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Sale)) {
			return false;
		}
		return getId() == ((Sale) obj).getId();
	}
}
