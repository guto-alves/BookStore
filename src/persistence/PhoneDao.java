package persistence;

import java.util.List;

public interface PhoneDao {

	public abstract int addPhones(List<String> phones, String cpf);

	public abstract int deletePhones(String cpf);
	
	public abstract void updatePhones(List<String> phones, String cpf);

	public abstract List<String> getAllPhones(String cpf);

}
