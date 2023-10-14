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


public class App extends Application {
    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//        EntityManager em = emf.createEntityManager();
//        System.out.println("Start a hibernate test");
//
//        em.getTransaction().begin();
//        Group gr = new Group();
//        gr.setName("Pink Floyd");
//        gr.setYearOfFoundation(1980);
//        em.persist(gr);
//        em.getTransaction().commit();
//        System.out.println("New group ID is: " + gr.getId());
        launch(args);


    }

//    @Override
//    public void start(Stage primaryStage) {
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(event -> System.out.println("Hello World!"));
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//
//        Scene scene = new Scene(root, 800, 600);
//
//        primaryStage.setTitle("Music Groups Manager");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("test.fxml")));
        primaryStage.getIcons().add(new Image("icons/app_icon.png"));
        primaryStage.setTitle("Music Band Manager");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }




}
