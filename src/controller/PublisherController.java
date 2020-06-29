package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import model.Publisher;
import persistence.PublisherDaoImpl;

public class PublisherController {
	private ObservableList<Publisher> publishersList;
	private ObjectProperty<Publisher> publisherSelected;

	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();
	
	private final PublisherDaoImpl publisherRepository;

	private boolean insertionMode = true;
	
	public PublisherController() {
		publisherRepository = new PublisherDaoImpl();
		
		publishersList = FXCollections.observableArrayList();
		publisherSelected = new SimpleObjectProperty<>();
		
		getAllPublishers();
	}
	
	public void onPublisherSelected(Publisher publisher) {
		insertionMode = publisher == null;
		publisherSelected.set(publisher);
	}

	public void onActionButtonPressed(String name, String phone, String street,
			String number, String complement) {
		if (insertionMode) {
			addPublisher(name, phone, street, number, complement);
		} else {
			updatePublisher(name, phone, street, number, complement);
		}
	}
	
	public void addPublisher(String name, String phone, String street,
			String number, String complement) {
		Publisher publisher = new Publisher(name, phone, street, number,
				complement);
		
		int result = publisherRepository.addPublisher(publisher);

		if (result == 1) {
			getAllPublishers();
			onPublisherSelected(null);
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Publisher Added", "New Publisher successfully added." });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR,
					"Publisher Not Added", "Error adding publisher." });
		}
	}

	public void updatePublisher(String name, String phone, String street,
			String number, String complement) {
		Publisher publisher = new Publisher(name, phone, street, number,
				complement);
		
		int result = publisherRepository.updatePublisher(
				publisher, publisherSelected.get().getName());

		if (result == 1) {
			getAllPublishers();
			onPublisherSelected(null);
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
							"Publisher Updated", 
							"Publisher successfully updated." });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, 
					"Publisher Not Updated", 
					"Unable to update publisher." });
		}
	}

	public void deletePublisher() {
		if (publisherSelected == null) {
			return;
		}

		int result = publisherRepository.deletePublisher(
				publisherSelected.get().getName());

		if (result == 1) {
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
							"Publisher Deleted",
							"Publisher successfully deleted!" });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR,
					"Publisher Not Excluded",
					"Could not delete selected publisher!" });
		}

		publishersList.remove(publisherSelected.get());
	}

	private void getAllPublishers() {
		publishersList.setAll(publisherRepository.getAllPublishers());
	}
	
	public ObservableList<Publisher> getPublishersList() {
		return publishersList;
	}

	public ObjectProperty<Publisher> getPublisherSelected() {
		return publisherSelected;
	}
	
	public ObjectProperty<Object[]> getWarningInfo() {
		return warningInfo;
	}
	
	public boolean isInsertionMode() {
		return insertionMode;
	}

}
