package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class MainMenuView { 
	private BorderPane root;

	private TabPane tabPane;
	
	private ListView<Tab> listView;

	public MainMenuView() {
		root = new BorderPane();
		
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

		MenuItem newTextFile = new MenuItem("New File");
		newTextFile.setOnAction(event -> {
			Tab annotationsTab = new Tab("Annotations");
			annotationsTab.setContent(new AnnotationsView()); 
			addTab(annotationsTab);
		});

		MenuItem openTextFile = new MenuItem("Open File");
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(event -> {
			System.exit(0);
		});
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(newTextFile, openTextFile, 
				new SeparatorMenuItem(), exitMenuItem);

		Menu settingsMenu = new Menu("Settings");
		Menu helpMenu = new Menu("Help");
		MenuBar menuBar = new MenuBar(fileMenu, settingsMenu, helpMenu);
		
		Tab salesTab = new Tab("Sales", new SaleView());
		Tab customersTab = new Tab("Customers", new CustomerView());
		Tab booksTab = new Tab("Books", new BookView());
		Tab categoriesTab = new Tab("Categories", new CategoryView());
		Tab authorsTab = new Tab("Authors", new AuthorView());
		Tab publishersTab = new Tab("Publishers", new PublisherView());
		Tab employeesTab = new Tab("Employees", new EmployeeView());

		listView = new ListView<>();
		listView.setMaxHeight(ListView.USE_COMPUTED_SIZE);
		listView.getItems().addAll(salesTab, customersTab, booksTab,
				categoriesTab, authorsTab, publishersTab, employeesTab);
		
		listView.setCellFactory(new Callback<ListView<Tab>, ListCell<Tab>>() {
			
			@Override
			public ListCell<Tab> call(ListView<Tab> param) {
				return new ListCell<Tab>() {
					@Override
					protected void updateItem(Tab item, boolean empty) {
						super.updateItem(item, empty);
				
						if (empty || item == null) {
							setGraphic(null);
						} else {
							Button button = new Button(item.getText());
							button.setMaxWidth(Double.MAX_VALUE);
							button.setOnAction(event -> {
								addTab(item);
							});
							setGraphic(button);
						}
					}
				};
			} 
		});
	
		Text text = new Text("Menu");
		text.setFill(Color.BLUE); 
		text.setFont(Font.font("Tamoha", FontWeight.BOLD, 16));
		
		VBox vBox = new VBox(text, listView);
		vBox.setAlignment(Pos.TOP_CENTER);
		
		SplitPane splitPane = new SplitPane(vBox, tabPane);
		splitPane.setDividerPositions(0.08f, 0.92f);

		root.setTop(menuBar);
		root.setCenter(splitPane);
		
		addTab(salesTab);
		addTab(booksTab);
	}

	private void addTab(Tab tab) {
		if (!tabPane.getTabs().contains(tab)) {
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		}
		
		tabPane.getSelectionModel().select(tab);
	}
	
	public BorderPane getRoot() {
		return root;
	}

}
