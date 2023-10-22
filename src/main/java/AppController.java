import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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
    private ObservableList<Group> data = FXCollections.observableArrayList();

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
            data.clear();
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
        final String[] name = new String[1];
        final Integer[] year = new Integer[1];
        final String[] genre = new String[1];
        final Integer[] place = new Integer[1];
        // logic for adding a band
        Stage newStage = new Stage();
        System.out.println("Adding band...");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane, 400, 300);

        // Create labels and text fields for each form
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label yearLabel = new Label("Year:");
        TextField yearTextField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreTextField = new TextField();

        Label placeLabel = new Label("Place:");
        TextField placeTextField = new TextField();
        Button okButton = new Button("OK");

        // Add labels and text fields to the grid pane
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);

        gridPane.add(yearLabel, 0, 1);
        gridPane.add(yearTextField, 1, 1);

        gridPane.add(genreLabel, 0, 2);
        gridPane.add(genreTextField, 1, 2);

        gridPane.add(placeLabel, 0, 3);
        gridPane.add(placeTextField, 1, 3);

        gridPane.add(okButton,1,4);
        // Set event handlers to save the entered text to variables
        okButton.setOnAction(event -> {
            name[0] = nameTextField.getText();
            year[0] = Integer.valueOf(yearTextField.getText());
            genre[0] = genreTextField.getText();
            place[0] = Integer.valueOf(placeTextField.getText());
            saveBandToDB(name[0],year[0],genre[0],place[0]);
            newStage.close();
        });
        newStage.setScene(scene);
        newStage.setTitle("Add Band");
        newStage.show();

    }
    private void saveBandToDB(String name,Integer year,String genre,Integer place) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        System.out.println("Savin new band to DataBase");

        em.getTransaction().begin();
        Group gr = new Group();
        gr.setName(name);
        gr.setYearOfFoundation(year);
        gr.setMainGenre(genre);
        gr.setPlaceInChart(place);
        em.persist(gr);
        em.getTransaction().commit();
        initialize();
        System.out.println("Band saved successfully!");
        System.out.println("New group ID is: " + gr.getId());
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