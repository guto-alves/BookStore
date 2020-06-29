package model;

public class Author {
	private int id;
	private String firstName;
	private String lastName;

	public Author(String firstName, String lastName) {
		this(0, firstName, lastName);
	}
	
	public Author(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Author)) {
			return false;
		}
		Author author = (Author) obj;
		return getId() == author.getId();
	}

	@Override
	public String toString() {
		return getLastName() + ", " + getFirstName();
	}
}
