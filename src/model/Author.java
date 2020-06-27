package model;

public class Author {
	private String firstName;
	private String lastName;

	public Author(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
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
		return getFirstName().equals(author.getFirstName()) && 
				getLastName().equals(author.getLastName());
	}

	@Override
	public String toString() {
		return String.format("%s %s", firstName, lastName);
	}
}
