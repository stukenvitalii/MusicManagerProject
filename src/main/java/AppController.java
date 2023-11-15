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
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Button saveButton;
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
    private ObservableList<Group> data = FXCollections.observableArrayList();
    List<Group> groups = new ArrayList<>();

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addGroup());
        removeButton.setOnAction(event -> removeGroup());
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
        exportXMLbutton.setOnAction(event -> {
            try {
                exportXML(groups);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setContentText("XML file {groups.xml} successfully exported!");
                alert.showAndWait();
            } catch (ParserConfigurationException | TransformerException | IOException e) {
                System.out.println(e.getMessage());
            }
        });
        generateReport.setOnAction(event -> {
            new XMLtoPDFReporter().createReport("groups.xml");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("Report file {report.pdf} created");
            alert.showAndWait();
        });
        try (EntityManager entityManager = Persistence.createEntityManagerFactory("test").createEntityManager()) {
            entityManager.getTransaction().begin();
            groups = entityManager.createQuery("from Group", Group.class).getResultList();
            data.clear();
            data.addAll(groups);
            entityManager.getTransaction().commit();
        }

    }

    private void addGroup() {
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
                saveGroupToDB(newGroup);
                newStage.close();
            } catch (NumberFormatException nfe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong number format");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + nfe.getMessage().toLowerCase());
                alert.showAndWait();
            } catch (IllegalArgumentException iae) {
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

    private void saveGroupToDB(Group group) {
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

    private void removeGroup() {
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

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(xml);

        document.getDocumentElement().normalize();

        NodeList groupsNodeList = document.getElementsByTagName("group");

        for (int i = 0; i < groupsNodeList.getLength(); i++) {
            Node elem = groupsNodeList.item(i);
            NamedNodeMap attributes = elem.getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String yearOfFoundation = attributes.getNamedItem("yearOfFoundation").getNodeValue();
            String genre = attributes.getNamedItem("genre").getNodeValue();
            String place = attributes.getNamedItem("place").getNodeValue();

            Group group = new Group();
            group.setName(name);
            group.setYearOfFoundation(Integer.valueOf(yearOfFoundation));
            group.setMainGenre(genre);
            group.setPlaceInChart(Integer.valueOf(place));
            saveGroupToDB(group);
        }
    }


    public void exportXML(List<Group> groupsSaved) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        Node groupsList = document.createElement("groups");
        document.appendChild(groupsList);
        for (Group group : groupsSaved) {
            Element groupEl = document.createElement("group");
            groupsList.appendChild(groupEl);
            groupEl.setAttribute("name", group.getName());
            groupEl.setAttribute("yearOfFoundation", group.getYearOfFoundation().toString());
            groupEl.setAttribute("genre", group.getMainGenre());
            groupEl.setAttribute("place", group.getPlaceInChart().toString());
        }
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        try(FileWriter fileWriter = new FileWriter("groups.xml")) {
            trans.transform(new DOMSource(document), new StreamResult(fileWriter));
        }
    }
}