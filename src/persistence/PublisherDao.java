package persistence;

import java.util.List;

import model.Publisher;

public interface PublisherDao {

	public abstract void add(Publisher publisher);

	public abstract void update(Publisher publisher);

	public abstract void remove(Publisher publisher);

	public abstract List<Publisher> selectAll();

}
