package model;

public class Publisher {
	private String name;
	private String phone;
	private String street;
	private String number;
	private String complement;

	public Publisher() {
	}
	
	public Publisher(String name, String phone,
			String street, String number, String complement) {
		this.name = name;
		this.phone = phone;
		this.street = street;
		this.number = number;
		this.complement = complement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Publisher)) {
			return false;
		}
		return getName().equals(((Publisher) obj).getName());
	}

	@Override
	public String toString() {
		return name;
	}

}
