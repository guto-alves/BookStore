package view;

import controller.BookController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Author;
import model.Publisher;
import model.Book;

public class BookView {
	private BorderPane root = new BorderPane();

	public BookView() {
		TableView<Book> tableView = new TableView<Book>();
		TextField filtroTextField = new TextField();
		FlowPane flowPane = new FlowPane(8, 8, new Label("Filtrar"), filtroTextField);
		flowPane.setPadding(new Insets(16));
		BorderPane borderPane = new BorderPane();
		borderPane.setPadding(new Insets(8));
		borderPane.setTop(flowPane);
		borderPane.setCenter(tableView);

		TextField isbnTextField = new TextField();
		TextField tituloTextField = new TextField();
		TextArea descricaoTextArea = new TextArea();
		descricaoTextArea.setPrefWidth(200);
		descricaoTextArea.setPrefHeight(80);
		TextField edicaoTextField = new TextField();
		TextField anoTextField = new TextField();
		ComboBox<Publisher> editorasComboBox = new ComboBox<>();
		ComboBox<Author> autoresComboBox = new ComboBox<>();
		Button addAutorButton = new Button(" >>> ");
		ListView<Author> autoresListView = new ListView<>();
		autoresListView.setPrefHeight(50);
		Button salvarButton = new Button("Adicionar");
		Button cancelarButton = new Button("Cancelar");
	
		GridPane.setHalignment(salvarButton, HPos.RIGHT);
		
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.addRow(0, new Label("ISBN"), isbnTextField);
		gridPane.addRow(1, new Label("Título"), tituloTextField);
		gridPane.addRow(2, new Label("Descrição"), descricaoTextArea);
		gridPane.addRow(3, new Label("Edição"), edicaoTextField);
		gridPane.addRow(4, new Label("Ano"), anoTextField);
		gridPane.addRow(5, new Label("Editora"), editorasComboBox);
		gridPane.addRow(6, new Label("Autor(es)"),
				new VBox(new HBox(16, autoresComboBox, addAutorButton),
						autoresListView));
		gridPane.add(new HBox(16, cancelarButton, salvarButton), 1, 7);
		
		root.setCenter(new SplitPane(borderPane, gridPane));
		
		new BookController(isbnTextField, tituloTextField, descricaoTextArea, 
				edicaoTextField, anoTextField, editorasComboBox, autoresComboBox, 
				autoresListView, addAutorButton, cancelarButton, salvarButton, 
				filtroTextField, tableView);	
	}

	public BorderPane getRoot() {
		return root;
	}

}
