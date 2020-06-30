package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import model.Publisher;
import persistence.DAOFactory;
import persistence.PublisherDao;

public class PublisherController {
	private StringProperty name = new SimpleStringProperty();
	private StringProperty phone = new SimpleStringProperty();
	private StringProperty street = new SimpleStringProperty();
	private StringProperty number = new SimpleStringProperty();
	private StringProperty complement = new SimpleStringProperty();

	private ObservableList<Publisher> publishersList;
	private ObjectProperty<Publisher> publisherSelected;

	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();

	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);
	
	private final PublisherDao publisherRepository;

	public PublisherController() {
		publisherRepository = DAOFactory.getPublisherDao();

		publishersList = FXCollections.observableArrayList();
		publisherSelected = new SimpleObjectProperty<>();

		getAllPublishers();
	}

	public void setPublisherSelected(Publisher publisher) {
		publisherSelected.set(publisher);
		
		if (publisher == null) {
			name.set("");
			street.set("");
			number.set("");
			complement.set("");
			phone.set("");
			insertionMode.set(true);
		} else {
			name.set(publisher.getName());
			street.set(publisher.getStreet());
			number.set(publisher.getNumber());
			complement.set(publisher.getComplement());
			phone.set(publisher.getPhone());
			insertionMode.set(false);
		}
	}

	public void onActionButtonPressed() {
		if (insertionMode.get()) {
			addPublisher();
		} else {
			updatePublisher();
		}
	}

	public void addPublisher() {
		Publisher publisher = new Publisher(name.get(), phone.get(), 
				street.get(), number.get(), complement.get());

		int result = publisherRepository.addPublisher(publisher);

		if (result == 1) {
			getAllPublishers();
			setPublisherSelected(null);
			warningInfo.set(
					new Object[] { AlertType.INFORMATION, "Publisher Added",
							"New Publisher successfully added." });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, "Publisher Not Added",
					"Error adding publisher." });
		}
	}

	public void updatePublisher() {
		Publisher publisher = new Publisher(name.get(), phone.get(),
				street.get(), number.get(), complement.get());

		int result = publisherRepository.updatePublisher(
				publisher, publisherSelected.get().getName());

		if (result == 1) {
			getAllPublishers();
			setPublisherSelected(publisher);
			warningInfo.set(
					new Object[] { AlertType.INFORMATION, 
							"Publisher Updated", 
							"Publisher successfully updated." });
		} else {
			warningInfo.set(
					new Object[] { AlertType.ERROR,
					"Publisher Not Updated", 
					"Unable to update publisher." });
		}
	}

	public void deletePublisher() {
		if (publisherSelected == null) {
			return;
		}

		int result = publisherRepository.deletePublisher(
				publisherSelected.get());

		if (result == 1) {
			warningInfo.set(
					new Object[] { AlertType.INFORMATION, "Publisher Deleted", 
							"Publisher successfully deleted!" });
		} else {
			warningInfo.set(
					new Object[] { AlertType.ERROR, "Publisher Not Excluded", 
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

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public StringProperty getPhone() {
		return phone;
	}

	public StringProperty getStreet() {
		return street;
	}

	public StringProperty getNumber() {
		return number;
	}

	public StringProperty getComplement() {
		return complement;
	}
	
	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}
	
	public ObjectProperty<Object[]> getWarningInfo() {
		return warningInfo;
	}
}
