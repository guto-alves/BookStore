package model;

public enum Role {
	CLERK("Clerk"), ADMIN("Admin");

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
