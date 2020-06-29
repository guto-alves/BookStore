package persistence;

import java.util.List;

public interface PhoneDao {

	public abstract void add(String phone);

	public abstract void update(String phone);

	public abstract void remove(String phone);

	public abstract List<String> selectAll();

}
