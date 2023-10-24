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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
    private TableColumn<Group,Integer> groupId;
    @FXML
    private ObservableList<Group> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addBand());
        removeButton.setOnAction(event -> removeBand());
        saveButton.setOnAction((event -> save()));
        searchButton.setOnAction(event -> search());
        listButton.setOnAction(event -> showList());
        try(EntityManager entityManager = Persistence.createEntityManagerFactory("test").createEntityManager()) {
            entityManager.getTransaction().begin();
            List<Group> groups = entityManager.createQuery("from Group",Group.class).getResultList();
            data.clear();
            data.addAll(groups);
            entityManager.getTransaction().commit();
        }
    }

    private void addBand() {
        final String[] name = new String[1];
        final String[] year = new String[1];
        final String[] genre = new String[1];
        final String[] place = new String[1];

        Stage newStage = new Stage();
        newStage.getIcons().add(new Image("icons/add_group.png"));
        System.out.println("Adding band...");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane, 300, 200);

        // Create labels and text fields for each form
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label yearLabel = new Label("Year of Foundation:");
        TextField yearTextField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreTextField = new TextField();

        Label placeLabel = new Label("Place in chart:");
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

        okButton.setOnAction(event -> {
            try {
                name[0] = validateInput(nameTextField.getText(), "Name");
                year[0] = validateInput(yearTextField.getText(), "Year of foundation");
                genre[0] = validateInput(genreTextField.getText(), "Genre");
                place[0] = validateInput(placeTextField.getText(), "Place in chart");
                saveBandToDB(name[0], Integer.valueOf(year[0]), genre[0], Integer.valueOf(place[0]));
                newStage.close();
            }catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong number format");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
            }
            catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input error");
                alert.setHeaderText(null);
                alert.setContentText(iae.getMessage());
                alert.showAndWait();
            }
        });

        newStage.setScene(scene);
        newStage.setTitle("Add Band");
        newStage.show();

    }
    private void saveBandToDB(String name,Integer year,String genre,Integer place) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        System.out.println("Saving new band to DataBase");

        em.getTransaction().begin();
        Group gr = new Group();
        gr.setName(name);
        gr.setYearOfFoundation(year);
        gr.setMainGenre(genre);
        gr.setPlaceInChart(place);
        em.persist(gr);
        em.getTransaction().commit();
        initialize();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Band successfully added, " + "id is " + gr.getId());
        alert.showAndWait();
    }
    private void removeBand() {
        // TODO add logic for removing
        System.out.println("Removing band...");
    }
    private void save() {
        // TODO add logic for saving
        System.out.println("Saving...");
    }
    private void search() {
        // TODO add logic for searching
        System.out.println("Searching...");
    }
    private void showList() {
        groupTableView.setItems(data);
        groupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupYearOfFoundationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfFoundation"));
        groupMainGenreColumn.setCellValueFactory(new PropertyValueFactory<>("mainGenre"));
        groupPlaceInChartColumn.setCellValueFactory(new PropertyValueFactory<>("placeInChart"));
    }

    public static class IllegalArgumentException extends Exception {
        public IllegalArgumentException(String message) {
            super(message);
        }
    }
    private String validateInput(String input, String fieldName) throws IllegalArgumentException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Field " + fieldName + " is empty. Try again.");
        }
        return input;
    }
}