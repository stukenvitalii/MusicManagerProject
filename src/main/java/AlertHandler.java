import javafx.scene.control.Alert;

public class AlertHandler {
    public static void makeAlertWindow(Alert.AlertType alertType,String title,String headerText,String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
