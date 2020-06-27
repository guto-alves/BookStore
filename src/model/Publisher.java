package model;

public class Publisher {
	private String name;
	private String address;
	private String phone;

	public Publisher(String name) {
		this(name, null, null);
	}

	public Publisher(String name, String address, String phone) {
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Publisher)) {
			return false;
		}
		return getName().equals(((Publisher)obj).getName());
	}
	
}
