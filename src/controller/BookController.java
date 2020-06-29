package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Author;
import model.Publisher;
import persistence.AuthorBookDaoImpl;
import persistence.AuthorDaoImpl;
import persistence.BookDaoImpl;
import persistence.CategoryDaoImpl;
import persistence.PublisherDaoImpl;
import model.Book;
import model.Category;

public class BookController {
	private StringProperty isbn = new SimpleStringProperty();
	private StringProperty title = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private StringProperty editionNumber = new SimpleStringProperty();
	private StringProperty year = new SimpleStringProperty();
	private StringProperty price = new SimpleStringProperty();
	private StringProperty copies = new SimpleStringProperty();

	private ObservableList<Publisher> publishers;
	private ObservableList<Author> authorsSelected;
	private ObservableList<Author> authors;
	private ObservableList<Category> categories;

	private ObservableList<Book> books;

	private Book bookSelected;
	private ObjectProperty<Publisher> publisherSelected = new SimpleObjectProperty<>();
	private ObjectProperty<Category> categorySelected = new SimpleObjectProperty<>();

	private final BookDaoImpl bookDao;
	private final PublisherDaoImpl publisherDao;
	private final AuthorDaoImpl authorDao;
	private final AuthorBookDaoImpl authorBookDaoImpl;
	private final CategoryDaoImpl categoryDao;

	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);
	
	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();

	public BookController() {
		bookDao = new BookDaoImpl();
		categoryDao = new CategoryDaoImpl();
		publisherDao = new PublisherDaoImpl();
		authorDao = new AuthorDaoImpl();
		authorBookDaoImpl = new AuthorBookDaoImpl();

		publishers = FXCollections.observableArrayList();
		authors = FXCollections.observableArrayList();
		categories = FXCollections.observableArrayList();
		books = FXCollections.observableArrayList();

		publishers.addAll(publisherDao.getAllPublishers());
		authors.addAll(authorDao.getAllAuthors());
		categories.addAll(categoryDao.getAllCategories());
		books.addAll(bookDao.getAllBooks());
		
		authorsSelected = FXCollections.observableArrayList();
	}

	public void setBookSelected(Book book) {
		bookSelected = book;

		if (bookSelected == null) {
			isbn.set("");
			title.set("");
			description.set("");
			editionNumber.set("");
			year.set("");
			price.set("");
			copies.set("");
			
			publisherSelected.set(null);
			categorySelected.set(null);
			authorsSelected.clear();
			
			insertionMode.set(true);
		} else {
			isbn.set(bookSelected.getIsbn());
			title.set(bookSelected.getTitle());
			description.set(bookSelected.getDescription());
			editionNumber.set(String.valueOf(bookSelected.getEditionNumber()));
			year.set(String.valueOf(bookSelected.getYear()));
			price.set(String.valueOf(bookSelected.getPrice()));
			copies.set(String.valueOf(bookSelected.getCopies()));
			
			publisherSelected.set(bookSelected.getPublisher());
			categorySelected.set(bookSelected.getCategory());
			authorsSelected.setAll(
					authorBookDaoImpl.getAllAuthorsOfTheBook(isbn.get()));
			
			insertionMode.set(false);
		}
	}

	public void addBook() {
		Book book = new Book(isbn.get(), title.get(), description.get(), 
				Integer.parseInt(editionNumber.get()),
				year.get(), Double.parseDouble(price.get()),
				Integer.parseInt(copies.get()), categorySelected.get(),
				publisherSelected.get());

		int result = bookDao.addBook(book);

		authorBookDaoImpl.add(isbn.get(), authorsSelected);

		if (result == 1) {
			books.add(book);
			setBookSelected(null);
			
			displayAlert(AlertType.INFORMATION, "Book Added",
					"Book successfully added.");
		} else {
			displayAlert(AlertType.ERROR, "Book Not Added",
					"Unable to add book.");
		}
	}

	public void updateBook() {
		Book book = new Book(isbn.get(), title.get(), description.get(), 
				Integer.parseInt(editionNumber.get()),
				year.get(), Double.parseDouble(price.get()),
				Integer.parseInt(copies.get()), categorySelected.get(),
				publisherSelected.get());
		
		int result = bookDao.updateBook(book, bookSelected.getIsbn());

		authorBookDaoImpl.update(authorsSelected, isbn.get());

		if (result == 1) {
			books.setAll(bookDao.getAllBooks());
			
			displayAlert(AlertType.INFORMATION, "Book Updated",
					"Book successfully updated.");
		} else {
			displayAlert(AlertType.ERROR, "Book Not Updated",
					"Unable to update book.");
		}
	}

	public void deleteBook() {
		if (bookSelected == null) {
			return;
		}

		int result = bookDao.deleteBook(bookSelected.getIsbn());

		if (result == 1) {
			books.remove(bookSelected);

			displayAlert(AlertType.INFORMATION, "Book Deleted",
					"Book successfully deleted.");
		} else {
			displayAlert(AlertType.ERROR, "Book Not Deleted", 
					"Unable to delete book.");
		}
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}

	// data source
	public ObservableList<Book> getBooksList() {
		return books;
	}

	public ObservableList<Publisher> getPublishers() {
		return publishers;
	}
	
	public ObservableList<Author> getAuthors() {
		return authors;
	}
	
	public ObservableList<Category> getCategories() {
		return categories;
	}
	
	public ObjectProperty<Object[]> getWarningInfo() {
		return warningInfo;
	}
	
	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}

	
	// selected
	public ObservableList<Author> getAuthorsSelected() {
		return authorsSelected;
	}

	public ObjectProperty<Category> getCategorySelected() {
		return categorySelected;
	}

	public ObjectProperty<Publisher> getPublisherSelected() {
		return publisherSelected;
	}

	// fields
	public StringProperty getIsbn() {
		return isbn;
	}

	public StringProperty getTitle() {
		return title;
	}

	public StringProperty getDescription() {
		return description;
	}

	public StringProperty getEditionNumber() {
		return editionNumber;
	}

	public StringProperty getYear() {
		return year;
	}

	public StringProperty getPrice() {
		return price;
	}

	public StringProperty getCopies() {
		return copies;
	}

}
