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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {
    @FXML
    public Button listButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button importXMLbutton;
    @FXML
    private Button exportXMLbutton;
    @FXML
    private Button generateReport;
    @FXML
    private TableView<Group> groupTableView;
    @FXML
    private TableColumn<Group, String> groupNameColumn;
    @FXML
    private TableColumn<Group, Integer> groupYearOfFoundationColumn;
    @FXML
    private TableColumn<Group, String> groupMainGenreColumn;
    @FXML
    private TableColumn<Group, Integer> groupPlaceInChartColumn;
    @FXML
    private TableColumn<Group, Integer> groupId;
    @FXML
    private ComboBox<String> comboBoxParameters;
    @FXML
    private TextField searchField;
    @FXML
    private ObservableList<Group> data = FXCollections.observableArrayList();
    List<Group> groups = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger("mainLogger");

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addGroup());
        removeButton.setOnAction(event -> removeGroup());
        editButton.setOnAction(event -> editInfo());
        searchButton.setOnAction(event -> search());
        listButton.setOnAction(event -> fillInTable(data));
        removeButton.setOnAction(event -> removeGroup());
        importXMLbutton.setOnAction(event -> {
            try {
                XMLfilesHandler.importXML();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                logger.error(e.getMessage(),e);
            }
        });
        exportXMLbutton.setOnAction(event -> {
            try {
                XMLfilesHandler.exportXML(groups);
                AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "XML file {groups.xml} successfully exported!");
                logger.info("XML file exported");
            } catch (ParserConfigurationException | TransformerException | IOException e) {
                logger.error(e.getMessage(),e);
            }
        });
        generateReport.setOnAction(event -> {
            XMLtoPDFReporter.createReport("groups.xml");
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Report file {report.pdf} created");
        });
        comboBoxParameters.getItems().setAll(
                "ID",
                "Название",
                "Год",
                "Жанр",
                "Место"
        );
        DataBaseHandler.getDataFromDB("test", data);
        groups = data;
        logger.info("System initialized");
    }

    private void addGroup() {
        logger.info("Started adding band");

        final String[] name = new String[1];
        final String[] year = new String[1];
        final String[] genre = new String[1];
        final String[] place = new String[1];

        Stage newStage = new Stage();
        newStage.getIcons().add(new Image("icons/add_group.png"));
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane, 300, 200);

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label yearLabel = new Label("Year of Foundation:");
        TextField yearTextField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreTextField = new TextField();

        Label placeLabel = new Label("Place in chart:");
        TextField placeTextField = new TextField();
        Button okButton = new Button("OK");

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);

        gridPane.add(yearLabel, 0, 1);
        gridPane.add(yearTextField, 1, 1);

        gridPane.add(genreLabel, 0, 2);
        gridPane.add(genreTextField, 1, 2);

        gridPane.add(placeLabel, 0, 3);
        gridPane.add(placeTextField, 1, 3);

        gridPane.add(okButton, 1, 4);

        okButton.setOnAction(event -> {
            try {
                name[0] = validateInput(nameTextField.getText(), "Name");
                year[0] = validateInput(yearTextField.getText(), "Year of foundation");
                genre[0] = validateInput(genreTextField.getText(), "Genre");
                place[0] = validateInput(placeTextField.getText(), "Place in chart");
                Group newGroup = new Group();
                newGroup.setName(name[0]);
                newGroup.setYearOfFoundation(Integer.valueOf(year[0]));
                newGroup.setMainGenre(genre[0]);
                newGroup.setPlaceInChart(Integer.valueOf(place[0]));
                DataBaseHandler.saveGroupToDB(newGroup);
                initialize();
                newStage.close();
            } catch (NumberFormatException nfe) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Wrong number format!", null, "Error: " + nfe.getMessage().toLowerCase());
                logger.error(nfe.getMessage(), nfe);
            } catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Input Error!", null, iae.getMessage());
                logger.error(iae.getMessage(),iae);
            }
        });

        newStage.setScene(scene);
        newStage.setTitle("Add Band");
        newStage.show();
    }

    private void removeGroup() {
        Group selectedGroup = groupTableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, "You should select a group!");
            logger.warn("Trying to delete group, but no group was selected");
        }
        else {
            int selectedGroupId = selectedGroup.getId();
            DataBaseHandler.deleteGroupById(selectedGroupId, data, "test");
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Deleted successfully", null, "Group " + selectedGroup.getName() + " was successfully delted from database!");
            logger.info("Group " + selectedGroup.getName() + " deleted successfully");
            initialize();
        }

    }

    private void editInfo() {
        Group selectedGroup = groupTableView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, "You should select a group!");
            logger.warn("Trying to edit group, but no group was selected");
        } else {
            int selectedGroupId = selectedGroup.getId();
            Map<String, String> newInfo = new HashMap<>();

            final String[] name = new String[1];
            final String[] year = new String[1];
            final String[] genre = new String[1];
            final String[] place = new String[1];

            Stage newStage = new Stage();
            newStage.getIcons().add(new Image("icons/edit_icon.png"));

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            Scene scene = new Scene(gridPane, 300, 200);

            Label nameLabel = new Label("Name:");
            TextField nameTextField = new TextField(selectedGroup.getName());

            Label yearLabel = new Label("Year of Foundation:");
            TextField yearTextField = new TextField(selectedGroup.getYearOfFoundation().toString());

            Label genreLabel = new Label("Genre:");
            TextField genreTextField = new TextField(selectedGroup.getMainGenre());

            Label placeLabel = new Label("Place in chart:");
            TextField placeTextField = new TextField(selectedGroup.getPlaceInChart().toString());
            Button okButton = new Button("Edit");

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameTextField, 1, 0);

            gridPane.add(yearLabel, 0, 1);
            gridPane.add(yearTextField, 1, 1);

            gridPane.add(genreLabel, 0, 2);
            gridPane.add(genreTextField, 1, 2);

            gridPane.add(placeLabel, 0, 3);
            gridPane.add(placeTextField, 1, 3);

            gridPane.add(okButton, 1, 4);

            newStage.setScene(scene);
            newStage.setTitle("Edit info");
            newStage.show();

            okButton.setOnAction(event -> {

                try {
                    name[0] = validateInput(nameTextField.getText(), "Name");
                    year[0] = validateInput(yearTextField.getText(), "Year of foundation");
                    genre[0] = validateInput(genreTextField.getText(), "Genre");
                    place[0] = validateInput(placeTextField.getText(), "Place in chart");

                    newInfo.put("name", name[0]);
                    newInfo.put("year", year[0]);
                    newInfo.put("genre", genre[0]);
                    newInfo.put("place", place[0]);

                    selectedGroup.setName(newInfo.get("name"));
                    selectedGroup.setYearOfFoundation(Integer.valueOf(newInfo.get("year")));
                    selectedGroup.setMainGenre(newInfo.get("genre"));
                    selectedGroup.setPlaceInChart(Integer.valueOf(newInfo.get("place")));

                    DataBaseHandler.editData(selectedGroupId, newInfo, "test");

                    AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Data edited successfully! Group " + selectedGroup.getName() + " ");
                    logger.info("Group info successfully edited");
                    newStage.close();
                    initialize();
                } catch (IllegalArgumentException e) {
                    AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, e.getMessage());
                    logger.error(e.getMessage(),e);
                }
            });
        }
    }

    public String getSelectedFromComboBox() {
        return comboBoxParameters.getSelectionModel().getSelectedItem();
    }

    private void search() {
        ObservableList<Group> resultData = FXCollections.observableArrayList();
        String selectedParameter = getSelectedFromComboBox();

        Map<String, String> selectedParamMap = new HashMap<>();
        selectedParamMap.put("ID", "group_id");
        selectedParamMap.put("Название", "group_name");
        selectedParamMap.put("Год", "group_year_of_foundation");
        selectedParamMap.put("Жанр", "group_main_genre");
        selectedParamMap.put("Место", "group_place_in_chart");

        DataBaseHandler.selectDataFromDB("test", selectedParamMap.get(selectedParameter), searchField.getText(), resultData);
        logger.info("Group found");
        fillInTable(resultData);
    }

    private void fillInTable(ObservableList<Group> data) {
        groupTableView.setItems(data);
        groupId.setCellValueFactory(new PropertyValueFactory<>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupYearOfFoundationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfFoundation"));
        groupMainGenreColumn.setCellValueFactory(new PropertyValueFactory<>("mainGenre"));
        groupPlaceInChartColumn.setCellValueFactory(new PropertyValueFactory<>("placeInChart"));
        logger.info("Table filled in");
    }

    public static class IllegalArgumentException extends Exception {
        public IllegalArgumentException(String message) {
            super(message);
        }
    }

    public String validateInput(String input, String fieldName) throws IllegalArgumentException, NumberFormatException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Field " + fieldName + " is empty. Try again.");
        }
        if ((fieldName.equals("Year of foundation") || fieldName.equals("Place in chart")) && !input.matches("-?\\d+(\\.\\d+)?")) {
            throw new NumberFormatException("Field " + fieldName + " should be an integer!");
        }
        return input;
    }
}