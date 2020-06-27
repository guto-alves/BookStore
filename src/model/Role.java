package model;

public enum Role {
	EMPLOYEE("Atendente"), LIBRARIAN("Bibliotec�rio");

	private final String name;

	private Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
