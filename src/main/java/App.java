import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    private static final Logger logger = LogManager.getLogger("mainLogger");
    public static void main(String[] args) {
        logger.info("Application started");
        launch(args);
        logger.info("Application closed");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test.fxml")));
        primaryStage.getIcons().add(new Image("icons/app_icon.png"));
        primaryStage.setTitle("Music Band Manager");
        primaryStage.setScene(new Scene(root, 750, 600));
        primaryStage.show();
    }
}
