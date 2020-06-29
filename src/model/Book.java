package model;

public class Book {
	private String isbn;
	private String title;
	private String description;
	private int editionNumber;
	private String year;
	private double price;
	private int copies;
	private Category category;
	private Publisher publisher;
	
	public Book() {
	}

	public Book(String isbn, String title, String description, 
			int editionNumber, String year, double price, int copies,
			Category category, Publisher publisher) {
		this.isbn = isbn;
		this.title = title;
		this.description = description;
		this.editionNumber = editionNumber;
		this.year = year;
		this.price = price;
		this.copies = copies;
		this.category = category;
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	@Override 
	public boolean equals(Object obj) {
		if (!(obj instanceof Book)) {
			return false;
		}
		
		return getIsbn().equals(((Book) obj).getIsbn());
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s", title, editionNumber, publisher.getName());
	}
}
