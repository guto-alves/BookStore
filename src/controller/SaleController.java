package controller;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import model.Book;
import model.Customer;
import model.EmployeeOn;
import model.Sale;
import persistence.DAOFactory;
import persistence.SaleBookDao;
import persistence.SaleDao;

public class SaleController {
	private static final String DISCOUNT_FORMAT = "Discount: %.0f %%";
	
	private StringProperty customerCpf = new SimpleStringProperty();
	private StringProperty customerName = new SimpleStringProperty();
	private StringProperty date = new SimpleStringProperty();
	private StringProperty total = new SimpleStringProperty();
	private StringProperty discount = new SimpleStringProperty();
	private StringProperty employeeName = new SimpleStringProperty();
	private ObservableList<Pair<Book, Integer>> books = 
			FXCollections.observableArrayList();

	private ObservableList<Sale> sales = FXCollections.observableArrayList();

	private Sale saleSelected;
	
	private final SaleDao saleDao;
	private SaleBookDao saleBookDao;
	
	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);
	
	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();

	public SaleController() {
		saleDao = DAOFactory.getSaleDao();
		saleBookDao = DAOFactory.getSaleBookDao();
		getAllSales();
		employeeName.set(EmployeeOn.employee.getName());
		total.set("0.0");
		discount.set(String.format(DISCOUNT_FORMAT, 0.0));
	}
	
	public void setBooksSelected(List<Book> booksSelected) {
		books.setAll(booksSelected.stream()
						.filter(book -> book.getCopies() > 0)
						.map(book -> new Pair<Book, Integer>(book, 1))
						.collect(Collectors.toList()));
					
		calculateTotal();
	}
	
	public void onSpinnerChanged(int quantity, Pair<Book, Integer> pair) {
		int index = books.indexOf(pair);
		
		books.set(index, new Pair<Book, Integer>(pair.getKey(), quantity));
		
		calculateTotal();
	}
	
	public void calculateTotal() {
		double currentTotal = 
				books.stream()
					.mapToDouble(pair -> pair.getKey().getPrice() * pair.getValue())
					.sum();
		
		int totalBooks = 
				books.stream()
					.mapToInt(pair -> pair.getValue())
					.reduce(0, (x, y) -> x + y);
		
		double discount = 0;
		
		if (totalBooks >= 10) {
			discount = 0.25;
		} else if (totalBooks >= 6) {
			discount = 0.15;
		} else if (totalBooks >= 3) {
			discount = 0.1;
		}    
		
		double newTotal = currentTotal - (currentTotal * discount);	
		
		total.set(String.valueOf(newTotal));
		this.discount.set(String.format(DISCOUNT_FORMAT, discount * 100));
	}

	public void setSaleSelected(Sale sale) {
		saleSelected = sale;
		
		if (saleSelected == null) {
			customerCpf.set("");
			customerName.set("");
			date.set("");
			total.set("0.0");
			discount.set(String.format(DISCOUNT_FORMAT, 0.0));
			employeeName.set(EmployeeOn.employee.getName());
			books.clear();
			insertionMode.set(true);
		} else {
			customerCpf.set(saleSelected.getCustomer().getCpf());
			customerName.set(saleSelected.getCustomer().getName());
			date.set(saleSelected.getDate());
			total.set(String.valueOf(saleSelected.getTotal()));			
			employeeName.set(saleSelected.getEmployee().getName());
			books.setAll(saleBookDao.getAllBooks(saleSelected.getId()));
			calculateTotal();
			insertionMode.set(false);
		}
	}
	
	public void onActionButtonPressed() {
		if (insertionMode.get()) {
			addSale();
		} else {
			updateSale(); 
		}
	}
	
	public void addSale() {
		Sale sale = new Sale(date.get(), Double.parseDouble(total.get()),
				new Customer(customerCpf.get()), EmployeeOn.employee);
		
		int result = saleDao.addSale(sale);

		if (result >= 1) {
			sale.setId(result);
			
			saleBookDao.addSaleBook(sale.getId(), books);
			
			getAllSales(); 
			setSaleSelected(null);
			
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Sale Added", "Sale successfully added." });
		} else {
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Sale Not Added", 
					"Unable to add sale." });
		}
	}

	public void updateSale() {
		Sale sale = new Sale(saleSelected.getId(), date.get(), 
				Double.parseDouble(total.get()),
				new Customer(customerCpf.get()), EmployeeOn.employee);
		
		int result = saleDao.updateSale(sale); 
		
		if (result == 1) {
			saleBookDao.updateSaleBook(sale.getId(), books); 
			
			getAllSales();
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Sale Updated", 
					"Sale successfully updated." });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, 
					"Sale Not Updated",
					"Unable to update sale." });
		}
	}
	
	public void deleteSale() {
		if (saleSelected == null) {
			return;
		}

		saleBookDao.deleteSaleBook(saleSelected.getId());
		
		int result = saleDao.deleteSale(saleSelected);

		if (result == 1) { 
			sales.remove(saleSelected);
			
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Sale Deleted",
					"Sale successfully deleted." });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, 
					"Sale Not Deleted",
					"Unable to delete sale." });
		}
	}

	private void getAllSales() {
		sales.setAll(saleDao.getAllSales());
	}


	public ObservableList<Sale> getSales() {
		return sales;
	}
	
	public ObservableList<Pair<Book, Integer>> getBooks() {
		return books;
	}
	
	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}
	
	public ObjectProperty<Object[]> getWarningInfo() {
		return warningInfo;
	}

	public Property<String> getCustomerCpf() {
		return customerCpf;
	}
	
	public StringProperty getCustomerName() {
		return customerName;
	}
	
	public StringProperty getDate() {
		return date;
	}

	public StringProperty getTotal() {
		return total;
	}
	
	public StringProperty getDiscount() {
		return discount;
	}

	public StringProperty getEmployeeName() {
		return employeeName;
	}
}
