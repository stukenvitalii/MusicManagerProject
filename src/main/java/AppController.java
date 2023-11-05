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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private Button importXMLbutton;
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
        importXMLbutton.setOnAction(event -> {
            try {
                importXML();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                System.out.println(e.getMessage());
            }
        });

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
                Group newGroup = new Group();
                newGroup.setName(name[0]);
                newGroup.setYearOfFoundation(Integer.valueOf(year[0]));
                newGroup.setMainGenre(genre[0]);
                newGroup.setPlaceInChart(Integer.valueOf(place[0]));
                saveBandToDB(newGroup);
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
    private void saveBandToDB(Group group) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        System.out.println("Saving new band to DataBase");

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
        initialize();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText("Band " + group.getName() + " successfully added, " + "id is " + group.getId());
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

    public void importXML() throws ParserConfigurationException, IOException, SAXException {
        Stage chooseFileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File xml = fileChooser.showOpenDialog(chooseFileStage);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(xml);

        NodeList groupsNodeList = document.getElementsByTagName("group");


        for (int i = 0; i < groupsNodeList.getLength(); i++) {
            Group group = new Group();
            if(groupsNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element groupElement = (Element) groupsNodeList.item(i);

                NodeList childNodes = groupElement.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {

                    if(childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) childNodes.item(j);
                        switch (childElement.getNodeName()) {
                            case "name": {
                                group.setName(childElement.getTextContent());
                                break;
                            }

                            case "yearOfFoundation": {
                                group.setYearOfFoundation(Integer.valueOf(childElement.getTextContent()));
                                break;
                            }

                            case "genre": {
                                group.setMainGenre(childElement.getTextContent());
                                break;
                            }

                            case "place": {
                                group.setPlaceInChart(Integer.valueOf(childElement.getTextContent()));
                                break;
                            }

                            default:
                                System.out.println("Something is wrong");
                        }
                    }

                }
            }
            saveBandToDB(group);
        }
    }


}