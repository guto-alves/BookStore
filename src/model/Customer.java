package model;

public class Customer {
	private String cpf;
	private String name;
	private String email;
	private String street;
	private String number;
	private String complement;
	private String zipCode;

	public Customer() {
	}

	public Customer(String cpf) {
		this.cpf = cpf;
	}

	public Customer(String cpf, String name, String email, 
			String street, String number, String complement,
			String zipCode) {
		this.cpf = cpf;
		this.name = name;
		this.email = email;
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.zipCode = zipCode;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Customer)) {
			return false;
		}

		return getCpf().equals(((Customer) obj).getCpf());
	}

}
