package controller;

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
import persistence.SaleBookDaoImpl;
import persistence.SaleDaoImpl;

public class SaleController {
	private StringProperty date = new SimpleStringProperty();
	private StringProperty total = new SimpleStringProperty("0.0");
	private StringProperty customerCpf = new SimpleStringProperty();
	private StringProperty customerName = new SimpleStringProperty();
	private StringProperty employeeName = new SimpleStringProperty();
	private ObservableList<Pair<Book, Integer>> books = FXCollections.observableArrayList();

	private ObservableList<Sale> sales = FXCollections.observableArrayList();

	private Sale saleSelected;
	
	private final SaleDaoImpl saleDao;
	private SaleBookDaoImpl saleBookDao;
	
	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);
	
	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();

	public SaleController() {
		saleDao = new SaleDaoImpl();
		saleBookDao = new SaleBookDaoImpl();
		getAllSales();
		employeeName.set(EmployeeOn.employee.getName());
	}

	public void setSaleSelected(Sale newValue) {
		saleSelected = newValue;
		
		if (saleSelected == null) {
			date.set("");
			total.set("0.0");
			customerName.set("");
			customerCpf.set("");
			employeeName.set(EmployeeOn.employee.getName());
			books.clear();
			insertionMode.set(true);
		} else {
			date.set(saleSelected.getDate());
			total.set(String.valueOf(saleSelected.getTotal()));
			customerName.set(saleSelected.getCustomer().getName());
			customerCpf.set(saleSelected.getCustomer().getCpf());
			employeeName.set(saleSelected.getEmployee().getName());
			books.setAll(saleBookDao.getAllBooks(saleSelected.getId()));
			insertionMode.set(false);
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

		int result = saleDao.deleteSale(saleSelected);

		if (result == 1) { 
			saleBookDao.deleteSaleBook(saleSelected.getId());
			
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

	// exposing data
	public ObservableList<Sale> getSales() {
		return sales;
	}
	
	public ObservableList<Pair<Book, Integer>> getBooks() {
		return books;
	}
	
	public ObjectProperty<Object[]> getWarningInfo() {
		return warningInfo;
	}

	// fields
	public StringProperty getDate() {
		return date;
	}

	public StringProperty getTotal() {
		return total;
	}

	public StringProperty getCustomerName() {
		return customerName;
	}

	public StringProperty getEmployeeName() {
		return employeeName;
	}

	public Property<String> getCustomerCpf() {
		return customerCpf;
	}
}
