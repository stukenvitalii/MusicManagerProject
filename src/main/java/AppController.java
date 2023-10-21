import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class AppController {
    public Button listButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label bandInfoLabel;
    @FXML
    private ListView<String> membersListView;
    @FXML
    private ListView<String> tourListView;
    @FXML
    private TableView<Group> groupTableView;
    @FXML
    private TableColumn<Group,String> groupNameColumn;
    @FXML
    private TableColumn<Group,Integer> groupYearOfFoundationColumn;
    @FXML
    private TableColumn<Group,String> groupMainGenreColumn;
    @FXML
    private TableColumn<Group,Integer> groupPlaceInChartColumn;
    @FXML
    ObservableList<Group> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addBand());
        removeButton.setOnAction(event -> removeBand());
        saveButton.setOnAction((event -> save()));
        searchButton.setOnAction(event -> search());
        listButton.setOnAction(event -> showList());

        try (EntityManager entityManager = Persistence.createEntityManagerFactory("test").createEntityManager()) {
            entityManager.getTransaction().begin();
            List<Group> groups = entityManager.createQuery("from Group",Group.class).getResultList();

            data.addAll(groups);
            entityManager.getTransaction().commit();
            entityManager.close();
            groupTableView.setItems(data);

            groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            groupYearOfFoundationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfFoundation"));
            groupMainGenreColumn.setCellValueFactory(new PropertyValueFactory<>("mainGenre"));
            groupPlaceInChartColumn.setCellValueFactory(new PropertyValueFactory<>("placeInChart"));
            }
        }




    private void addBand() {
        // logic for adding a band
        System.out.println("Adding band...");
        Stage newStage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root, 400, 300);
        Label titleLabel = new Label("Welcome to the New Window");
        Button closeButton = new Button("Close");

        root.getChildren().addAll(titleLabel, closeButton);

        newStage.setScene(scene);
        newStage.setTitle("New Window");
        newStage.show();

    }

    private void removeBand() {
        // logic for removing a band
        System.out.println("Removing band...");
    }
    private void save() {
        // logic for saving everything
        System.out.println("Saving...");
    }
    private void search() {
        // logic for searching
        System.out.println("Searching...");
    }
    private void showList() {
        // logic for showing List of bands
        System.out.println("Showing list of bands...");
    }
}