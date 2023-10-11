import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    public void initialize() {
        addButton.setOnAction(event -> addBand());
        removeButton.setOnAction(event -> removeBand());
        saveButton.setOnAction((event -> save()));
        searchButton.setOnAction(event -> search());
        listButton.setOnAction(event -> showList());
    }


    private void addBand() {
        // logic for adding a band
        System.out.println("Adding band...");
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