package persistence;

public class DAOFactory {

	public static BookDao getBookDao() {
		return new BookDaoImpl();
	}

	public static AuthorDao getAuthorDao() {
		return new AuthorDaoImpl();
	}
	
	public static AuthorBookDao getAuthorBookDao() {
		return new AuthorBookDaoImpl();
	}

	public static PublisherDao getPublisherDao() {
		return new PublisherDaoImpl();
	}

	public static CategoryDao getCategoryDao() {
		return new CategoryDaoImpl();
	}

	public static CustomerDao getCustomerDao() {
		return new CustomerDaoImpl();
	}
	
	public static SaleDao getSaleDao() {
		return new SaleDaoImpl();
	}
	
	public static SaleBookDao getSaleBookDao() {
		return new SaleBookDaoImpl();
	}

	public static EmployeeDao getEmployeeDao() {
		return new EmployeeDaoImpl();
	}

	public static PhoneDao getPhoneDao() {
		return new PhoneDaoImpl();
	}
}
