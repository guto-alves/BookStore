package view;

import controller.LoginController;
import javafx.application.Platform;
import javafx.concurrent.Task; 
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.EmployeeOn;

public class LoginView {
	private Dialog<Void> dialog;
	private Text title;
	private Text warningText;
	private final LoginController controller = new LoginController();
	
	private Callback<Boolean, Void> callback;

	public LoginView(Callback<Boolean, Void> callback) {
		this.callback = callback;
		
		dialog = new Dialog<>();
		dialog.setTitle("Book Store");
		dialog.getDialogPane().setBackground(new Background(
				new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		dialog.setResizable(true);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 0, 25));

		ColumnConstraints columnConstraints1 = new ColumnConstraints(60, 60, 60);
		ColumnConstraints columnConstraints2 = new ColumnConstraints(50, 250, 250);
		columnConstraints2.setMaxWidth(Double.MAX_VALUE);
		columnConstraints2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(columnConstraints1, columnConstraints2);

		title = new Text("Welcome");
		title.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

		TextField email = new TextField();
		email.setPromptText("Email");

		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		Button loginButton = new Button();
		loginButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		Text text = new Text("Sign in");
		text.setFont(Font.font("Serif", FontWeight.SEMI_BOLD, 16)); 
		text.setFill(Color.WHITE);
		loginButton.setGraphic(text);
		loginButton.setPadding(new Insets(5, 25, 5, 25));
		loginButton.setBackground(new Background(
				new BackgroundFill(Color.BLUE, new CornerRadii(5), Insets.EMPTY)));
		loginButton.setDisable(true);
		GridPane.setHalignment(loginButton, HPos.RIGHT);

		warningText = new Text();
		warningText.setFill(Color.FIREBRICK);
		warningText.setFont(Font.font(14));

		grid.add(title, 0, 0, 2, 1);
		grid.addRow(1, new Label("Email:"), email);
		grid.addRow(2, new Label("Password:"), password);
		grid.add(loginButton, 1, 3);
		grid.add(warningText, 1, 4);

		ButtonType buttonType = new ButtonType("Sign in", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonType);
		Node buttonTypeNode = dialog.getDialogPane().lookupButton(buttonType);
		buttonTypeNode.setDisable(true);
		buttonTypeNode.setManaged(false);
		buttonTypeNode.setVisible(false);

		email.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isBlank() || 
					password.getText().isBlank());
			warningText.setText("");
		});

		password.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isBlank() 
					|| email.getText().isBlank());
			warningText.setText("");
		});
		
		password.setOnAction(event -> {
			login(email.getText(), password.getText());
		});

		loginButton.setOnAction(event -> {
			login(email.getText(), password.getText());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> email.requestFocus());
	}
	
	private void login(String email, String password) {
		if (controller.login(email, password)) {
			title.setText("Welcome, " + EmployeeOn.employee.getName().split(" ")[0]);
			warningText.setFill(Color.BLUE);
			warningText.setText("Wait ...");

			Task<Void> close = new Task<>() {

				@Override
				protected Void call() throws Exception {
					Thread.sleep(2000);
					return null;
				}
			};
			new Thread(close).start();

			close.setOnSucceeded(value -> {
				dialog.close();
				callback.call(true);
			});
		} else {
			warningText.setText("Invalid email or password.");
			callback.call(false);
		}
	}

	public void show() {
		dialog.showAndWait();
	}
}
