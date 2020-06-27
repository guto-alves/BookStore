package model;

public class Customer {
	private String rg;
	private String cpf;
	private String name;
	private String email;
	private String street;
	private int number;
	private String complement;
	private String postalCode;

	public Customer(String rg) {
		this.rg = rg;
	}

	public Customer(String rg, String cpf, String name, String email, 
			String street, int number, String complement,
			String postalCode) {
		this.rg = rg;
		this.cpf = cpf;
		this.name = name;
		this.email = email;
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.postalCode = postalCode;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Customer)) {
			return false;
		}

		return getRg().equals(((Customer) obj).getRg());
	}

}
