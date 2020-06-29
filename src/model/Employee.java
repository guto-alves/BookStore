package model;

public class Employee {
	private int id;
	private String name;
	private String phone;
	private Role role;
	private String email;
	private String password;

	public Employee() {
	}

	public Employee(int id) {
		this.id = id;
	}

	public Employee(String name, String phone, Role role, String email,
			String password) {
		this(0, name, phone, role, email, password);
	}

	public Employee(int id, String name, String phone, Role role,
			String email, String password) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.role = role;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee)) {
			return false;
		}
		
		return getId() == ((Employee) obj).getId();
	}

}
