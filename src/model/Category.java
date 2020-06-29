package model;

public class Category {
	private int id;
	private String name;

	public Category(String name) {
		this.name = name;
	}

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Category)) {
			return false;
		}
		Category category = (Category) obj;
		return getId() == category.getId();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
