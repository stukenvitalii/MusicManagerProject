import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.*;
import org.hibernate.exception.SQLGrammarException;

public class App extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test.fxml")));
        primaryStage.getIcons().add(new Image("icons/app_icon.png"));
        primaryStage.setTitle("Music Band Manager");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }




}
