package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import model.Publisher;
import repository.PublisherRepository;

public class PublisherController {
	private ObservableList<Publisher> publishersList;
	private ObjectProperty<Publisher> publisherSelected = new SimpleObjectProperty<>();

	private ObjectProperty<Object[]> warningInfo = new SimpleObjectProperty<>();
	
	private boolean insertionMode = true;
	
	private final PublisherRepository publisherRepository;

	private boolean insertionResult;
	
	public PublisherController() {
		publisherRepository = new PublisherRepository();
		publishersList = FXCollections.observableArrayList();
		getAllPublishers();
	}
	
	public void onPublisherSelected(Publisher publisher) {
		insertionMode = publisher == null;
		publisherSelected.set(publisher);
	}

	public void onActionButtonPressed(String name, String address, String phone) {
		if (insertionMode) {
			addPublisher(name, address, phone);
		} else {
			updatePublisher(name, address, phone);
		}
	}
	
	public void addPublisher(String name, String address, String phone) {
		Publisher publisher = new Publisher(name, address, phone);
		
		int result = publisherRepository.addPublisher(publisher);

		if (result == 1) {
			getAllPublishers();
			publisherSelected.set(publisher);
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
					"Editora Adicionada", "Editora adicionada com sucesso!" });
		} else {
			insertionResult = false;
			warningInfo.set(new Object[] { AlertType.ERROR,
					"Editora Não Adicionada", "Erro ao adicionar editora!" });
		}
	}

	public void updatePublisher(String name, String address, String phone) {
		int result = publisherRepository.updatePublisher(
				name, address, phone, publisherSelected.get().getName());

		if (result == 1) {
			getAllPublishers();
			onPublisherSelected(null);
			warningInfo.set(new Object[] { AlertType.INFORMATION, 
							"Editora Atualizada", "Editora atualizada com sucesso!" });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, 
					"Editora Não Atualizada", "Erro ao atualizar editora!" });
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
							"Editora Excluído", "Editora excluída com sucesso!" });
		} else {
			warningInfo.set(new Object[] { AlertType.ERROR, "Editora Não Excluído",
					"Não foi possível excluir editora selecionada!" });
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
	
	public boolean getInsertResult() {
		return insertionResult;
	}

}
