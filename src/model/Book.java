package model;

public class Book {
	private String isbn;
	private String title;
	private String description;
	private int editionNumber;
	private int year;
	private Category category;
	private Publisher publisher;

	public Book(String isbn, String title, String description, 
			int editionNumber, int year, String nomeEditora) {
		this(isbn, title, description, editionNumber, year, null,
				new Publisher(nomeEditora));
	}

	public Book(String isbn, String title, String description, 
			int editionNumber, int year, Category category,
			Publisher publisher) {
		this.isbn = isbn;
		this.title = title;
		this.description = description;
		this.editionNumber = editionNumber;
		this.year = year;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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
	public String toString() {
		return String.format("%s, %s, %s", 
				title, editionNumber, publisher.getName());
	}

}
