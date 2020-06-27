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

		MenuItem newTextFile = new MenuItem("Novo Arquivo");
		newTextFile.setOnAction(event -> {
			Tab anotationsTab = new Tab("Anotações");
			anotationsTab.setContent(new AnotationsView().getRoot());
			addTab(anotationsTab);
		});

		MenuItem openTextFile = new MenuItem("Abrir Arquivo");
		MenuItem exitMenuItem = new MenuItem("Sair");
		exitMenuItem.setOnAction(event -> {
			System.exit(0);
		});
		Menu fileMenu = new Menu("Arquivo");
		fileMenu.getItems().addAll(newTextFile, openTextFile, 
				new SeparatorMenuItem(), exitMenuItem);

		Menu settingsMenu = new Menu("Configurações");
		Menu helpMenu = new Menu("Ajuda");
		MenuBar menuBar = new MenuBar(fileMenu, settingsMenu, helpMenu);
		
		Tab loansTab = new Tab("Empréstimo");
		loansTab.setContent(new LoanView().getRoot());

		Tab customersTab = new Tab("Clientes");
		customersTab.setContent(new CustomerView().getRoot());

		Tab booksTab = new Tab("Livros");
		booksTab.setContent(new BookView().getRoot());

		Tab authorsTab = new Tab("Autores");
		authorsTab.setContent(new AuthorView().getRoot());

		Tab publishersTab = new Tab("Editoras");
		publishersTab.setContent(new PublisherView().getRoot());

		Tab employeesTab = new Tab("Funcionários");
		employeesTab.setContent(new EmployeeView().getRoot());

		listView = new ListView<>();
		listView.setMaxHeight(ListView.USE_COMPUTED_SIZE);
		listView.getItems().addAll(loansTab, customersTab, booksTab,
				authorsTab, publishersTab, employeesTab);
		
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
		
		addTab(loansTab);
	}

	private void addTab(Tab tab) {
		if (!tabPane.getTabs().contains(tab)) {
			tabPane.getTabs().add(tab);
		}
		
		tabPane.getSelectionModel().select(tab);
	}
	
	public BorderPane getRoot() {
		return root;
	}

}
