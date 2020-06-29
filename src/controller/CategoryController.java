package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Category;
import persistence.CategoryDaoImpl;

public class CategoryController {
	private StringProperty name = new SimpleStringProperty();

	private ObservableList<Category> categorysList;
	private ObjectProperty<Category> categorySelected = new SimpleObjectProperty<>();

	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);

	private final CategoryDaoImpl categoryDao;

	public CategoryController() {
		categoryDao = new CategoryDaoImpl();
		categorysList = FXCollections.observableArrayList();
		getAllCategories();
	}

	public void setCategorySelected(Category category) {
		categorySelected.set(category);
		
		if (category == null) {
			name.set("");
			insertionMode.set(true);
		} else {
			name.set(category.getName());
			insertionMode.set(false);
		}
	}

	private boolean hasInvalidFields() {
		name.set(name.get().strip());
		return name.get().isBlank();
	}

	public void addCategory() {
		if (hasInvalidFields()) {
			return;
		}

		Category category = new Category(name.get());

		int result = categoryDao.addCategory(category);

		if (result == 1) {
			getAllCategories();
			setCategorySelected(null);

			displayAlert(AlertType.INFORMATION, "Category Added",
					"Category successfully added.");
		} else {
			displayAlert(AlertType.ERROR, "Category Not Added", 
					"Unable to add category.");
		}
	}

	public void updateAutor() {
		if (hasInvalidFields()) {
			return;
		}
		
		Category category = new Category(
				categorySelected.get().getId(), name.get());

		int result = categoryDao.updateCategory(category);

		if (result == 1) {
			getAllCategories();
			displayAlert(AlertType.INFORMATION, "Category Updated",
					"Category successfully updated.");
		} else {
			displayAlert(AlertType.ERROR, "Category Not Updated", 
					"Unable to update category.");
		}
	}

	public void deleteCategory() {
		if (categorySelected == null) {
			return;
		}

		int result = categoryDao.deleteCategory(categorySelected.get());

		if (result == 1) {
			categorysList.remove(categorySelected.get());
			
			displayAlert(AlertType.INFORMATION, "Category Deleted",
					"Category successfully deleted.");
		} else {
			displayAlert(AlertType.ERROR, "Category Not Deleted",
					"Unable to delete category.");
		}
	}

	private void getAllCategories() {
		categorysList.setAll(categoryDao.getAllCategories());
	}

	private void displayAlert(AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText("Message");
		alert.setContentText(message);
		alert.show();
	}

	public StringProperty getFirstName() {
		return name;
	}
	
	public ObservableList<Category> getCategorysList() {
		return categorysList;
	}

	public ObjectProperty<Category> getCategorySelected() {
		return categorySelected;
	}

	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}

}
