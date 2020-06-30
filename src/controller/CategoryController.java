package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Category;
import persistence.CategoryDao;
import persistence.DAOFactory;

public class CategoryController {
	private StringProperty name = new SimpleStringProperty();

	private ObservableList<Category> categorysList;
	private Category categorySelected;

	private BooleanProperty insertionMode = new SimpleBooleanProperty(true);

	private final CategoryDao categoryDao;

	public CategoryController() {
		categoryDao = DAOFactory.getCategoryDao();
		categorysList = FXCollections.observableArrayList();
		getAllCategories();
	}

	public void setCategorySelected(Category category) {
		categorySelected = category;
		
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
				categorySelected.getId(), name.get());

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

		int result = categoryDao.deleteCategory(categorySelected);

		if (result == 1) {
			categorysList.remove(categorySelected);
			
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

	public BooleanProperty getInsertionMode() {
		return insertionMode;
	}

}
