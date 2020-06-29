package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {
	
	public static void displayAlert(Object[] info) {
		displayAlert((AlertType) info[0], info[1].toString(), info[2].toString());
	}
	
	public static void displayAlert(AlertType alertType, String title,
			String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText("Message");
		alert.setContentText(message);
		alert.show();
	}

}
