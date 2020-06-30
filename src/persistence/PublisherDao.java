package persistence;

import java.util.List;

import model.Publisher;

public interface PublisherDao {

	public abstract int addPublisher(Publisher publisher);

	public abstract int updatePublisher(Publisher publisher, 
			String currentName);

	public abstract int deletePublisher(Publisher publisher);

	public abstract List<Publisher> getAllPublishers();

}
