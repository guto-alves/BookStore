package model;

import java.util.List;

public class Loan {
	private String loanDate;
	private String returnDate;
	private String dateReturned;
	private List<Book> books;
	private Customer customer;
	private Employee employee;

	public Loan(String loanDate, String returnDate, String dateReturned, 
			Customer customer, Employee employee) {
		this(loanDate, returnDate, dateReturned, null, customer, employee);
	}

	public Loan(String loanDate, String returnDate, String dateReturned, 
			List<Book> books, Customer customer, Employee employee) {
		this.loanDate = loanDate;
		this.returnDate = returnDate;
		this.dateReturned = dateReturned;
		this.books = books;
		this.customer = customer;
		this.employee = employee;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getDateReturned() {
		return dateReturned;
	}

	public void setDateReturned(String dateReturned) {
		this.dateReturned = dateReturned;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
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

}
