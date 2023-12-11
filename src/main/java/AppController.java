import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.SearchableComboBox;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AppController {
    @FXML
    private Button listButton;
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
    private Button getThreeBestGroups;
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
    private ObservableList<Group> groupsData = FXCollections.observableArrayList();

    private List<Group> groups = new ArrayList<>();

    private final List<String> availableGenres = Arrays.asList(
            "Rock",
            "Alternative Rock",
            "Alternative Metal",
            "Doom Metal",
            "Progressive Rock",
            "Hard Rock",
            "Industrial Metal",
            "Thrash Metal",
            "Ambient",
            "Doom Metal",
            "Black Metal",
            "Heavy Metal",
            "Punk Rock",
            "Psychedelic Rock",
            "Rock-n-Roll",
            "Symphonic Rock",
            "Jazz Rock",
            "Neue Deutsche Härte",
            "Gothic Rock",
            "Folk Rock",
            "Post Rock",
            "Pop",
            "Rap",
            "Hip-Hop",
            "Classic",
            "Disco"
    );
    private final ObservableList<String> observableListGenres = FXCollections.observableArrayList();
    private static final Logger logger = LogManager.getLogger("mainLogger");

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addGroup());
        removeButton.setOnAction(event -> removeGroup());
        editButton.setOnAction(event -> editInfoGroup());
        searchButton.setOnAction(event -> search());
        listButton.setOnAction(event -> fillInTable(groupsData));
        removeButton.setOnAction(event -> removeGroup());
        importXMLbutton.setOnAction(event -> {
            try {
                XMLfilesHandler.importXML();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        exportXMLbutton.setOnAction(event -> {
            try {
                XMLfilesHandler.exportXML(groups);
                AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "XML file {groups.xml} successfully exported!");
                logger.info("XML file exported");
            } catch (ParserConfigurationException | TransformerException | IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        generateReport.setOnAction(event -> {
            PDFReporter.createReportGroups("groups.xml");
            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Report file {report.pdf} created");
        });

        getThreeBestGroups.setOnAction(event -> {
            ObservableList<Group> threeBest = FXCollections.observableArrayList();
            for (Group group: groups) {
                if (group.getPlaceInChart() <= 3) {
                    threeBest.add(group);
                }
            }
            fillInTable(threeBest);
        });

        comboBoxParameters.getItems().setAll(
                "ID",
                "Name",
                "Year",
                "Genre",
                "Place"
        );
        comboBoxParameters.setValue("Name");
        groupTableView.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Group selectedGroup = groupTableView.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    showDetails(selectedGroup);
                }
            }
        });
        DataBaseHandler.getDataFromDB("test", groupsData);
        groups = groupsData;

        groupTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        groupTableView.setColumnResizePolicy((param) -> true);

        observableListGenres.addAll(availableGenres);

        fillInTable(groupsData);

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
        SearchableComboBox<String> comboBoxGenres = new SearchableComboBox<>();

        comboBoxGenres.setItems(observableListGenres);

        Label placeLabel = new Label("Place in chart:");
        TextField placeTextField = new TextField();
        Button okButton = new Button("OK");

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);

        gridPane.add(yearLabel, 0, 1);
        gridPane.add(yearTextField, 1, 1);

        gridPane.add(genreLabel, 0, 2);
        gridPane.add(comboBoxGenres, 1, 2);

        gridPane.add(placeLabel, 0, 3);
        gridPane.add(placeTextField, 1, 3);

        gridPane.add(okButton, 1, 4);

        okButton.setOnAction(event -> {
            try {
                name[0] = validateInputGroup(nameTextField.getText(), "Name");
                year[0] = validateInputGroup(yearTextField.getText(), "Year of foundation");
                genre[0] = validateInputGroup(comboBoxGenres.getSelectionModel().getSelectedItem(), "Genre");
                place[0] = validateInputGroup(placeTextField.getText(), "Place in chart");
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
                logger.warn(nfe.getMessage(), nfe);
            } catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Input Error!", null, iae.getMessage());
                logger.warn(iae.getMessage(), iae);
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
        } else {
            int selectedGroupId = selectedGroup.getId();

            logger.info("Confirming deletion");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            String s = "Confirm deleting the group. This is irreversible!";
            alert.setContentText(s);

            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                logger.info("Deleting confirmed");
                DataBaseHandler.deleteGroupById(selectedGroupId, groupsData, "test");
                AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Deleted successfully", null, "Group " + selectedGroup.getName() + " was successfully deleted from database!");
                logger.info("Group " + selectedGroup.getName() + " deleted successfully");
                initialize();
            }
            else {
                AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION,"Canceled",null,"Deleting the group was canceled");
                logger.info("Deleting the group canceled");
            }
        }
    }

    private void editInfoGroup() {
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
            SearchableComboBox<String> comboBoxGenres = new SearchableComboBox<>();

            comboBoxGenres.setItems(observableListGenres);
            comboBoxGenres.setValue(selectedGroup.getMainGenre());

            Label placeLabel = new Label("Place in chart:");
            TextField placeTextField = new TextField(selectedGroup.getPlaceInChart().toString());
            Button okButton = new Button("Edit");

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameTextField, 1, 0);

            gridPane.add(yearLabel, 0, 1);
            gridPane.add(yearTextField, 1, 1);

            gridPane.add(genreLabel, 0, 2);
            gridPane.add(comboBoxGenres, 1, 2);

            gridPane.add(placeLabel, 0, 3);
            gridPane.add(placeTextField, 1, 3);

            gridPane.add(okButton, 1, 4);

            newStage.setScene(scene);
            newStage.setTitle("Edit info");
            newStage.show();

            okButton.setOnAction(event -> {

                try {
                    name[0] = validateInputGroup(nameTextField.getText(), "Name");
                    year[0] = validateInputGroup(yearTextField.getText(), "Year of foundation");
                    genre[0] = validateInputGroup(comboBoxGenres.getSelectionModel().getSelectedItem(), "Genre");
                    place[0] = validateInputGroup(placeTextField.getText(), "Place in chart");

                    newInfo.put("name", name[0]);
                    newInfo.put("year", year[0]);
                    newInfo.put("genre", genre[0]);
                    newInfo.put("place", place[0]);

                    selectedGroup.setName(newInfo.get("name"));
                    selectedGroup.setYearOfFoundation(Integer.valueOf(newInfo.get("year")));
                    selectedGroup.setMainGenre(newInfo.get("genre"));
                    selectedGroup.setPlaceInChart(Integer.valueOf(newInfo.get("place")));

                    DataBaseHandler.editDataGroup(selectedGroupId, newInfo, "test");

                    AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Data edited successfully! Group " + selectedGroup.getName() + " ");
                    logger.info("Group info successfully edited");
                    newStage.close();
                    initialize();
                } catch (IllegalArgumentException e) {
                    AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, e.getMessage());
                    logger.warn(e.getMessage(), e);
                }
            });
        }
    }

    public String getSelectedFromComboBox() {
        return comboBoxParameters.getSelectionModel().getSelectedItem();
    }

    private void showDetails(Group group) {
        Stage groupDetailsStage = new Stage();

        ObservableList<GroupMember> membersData = FXCollections.observableArrayList();
        ObservableList<Tour> toursData = FXCollections.observableArrayList();
        ObservableList<Song> songsData = FXCollections.observableArrayList();

        groupDetailsStage.getIcons().add(new Image("icons/info_icon.png"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        Label nameLabel = new Label("Band Name: " + group.getName());
        Label yearLabel = new Label("Formation Year: " + group.getYearOfFoundation().toString());
        Label genreLabel = new Label("Genre: " + group.getMainGenre());
        Label chartPositionLabel = new Label("Chart Position: " + group.getPlaceInChart());

        TableView<GroupMember> membersTableView = new TableView<>();
        TableColumn<GroupMember, String> memberNameColumn = new TableColumn<>("Name");
        TableColumn<GroupMember, String> memberRoleColumn = new TableColumn<>("Role");
        TableColumn<GroupMember, String> memberAgeColumn = new TableColumn<>("Age");

        membersTableView.getColumns().addAll(memberNameColumn, memberRoleColumn, memberAgeColumn);

        Button addNewMemberButton = new Button("Add member");
        Button editMemberButton = new Button("Edit member");
        Button deleteMemberButton = new Button("Delete member");

        memberNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        memberRoleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        memberAgeColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());

        memberNameColumn.prefWidthProperty().bind(membersTableView.widthProperty().multiply(0.33));
        memberRoleColumn.prefWidthProperty().bind(membersTableView.widthProperty().multiply(0.33));
        memberAgeColumn.prefWidthProperty().bind(membersTableView.widthProperty().multiply(0.33));

        membersData.clear();
        membersData.addAll(group.getMembers());
        membersTableView.setItems(membersData);

        TableView<Tour> toursTableView = new TableView<>();
        TableColumn<Tour, String> tourNameColumn = new TableColumn<>("Name");
        TableColumn<Tour, String> tourDateBeginColumn = new TableColumn<>("Begin");
        TableColumn<Tour, String> tourDateEndColumn = new TableColumn<>("End");
        toursTableView.getColumns().addAll(tourNameColumn, tourDateBeginColumn, tourDateEndColumn);

        Button addNewTourButton = new Button("Add tour");
        Button editTourButton = new Button("Edit tour");
        Button deleteTourButton = new Button("Delete tour");
        Button exportToursReportButton = new Button("Export report");

        tourDateBeginColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfBeginningProperty());
        tourDateEndColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfEndProperty());
        tourNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        tourNameColumn.prefWidthProperty().bind(toursTableView.widthProperty().multiply(0.33));
        tourDateBeginColumn.prefWidthProperty().bind(toursTableView.widthProperty().multiply(0.33));
        tourDateEndColumn.prefWidthProperty().bind(toursTableView.widthProperty().multiply(0.33));

        toursData.clear();
        toursData.addAll(group.getTours());
        toursTableView.setItems(toursData);

        TableView<Song> songsTableView = new TableView<>();
        TableColumn<Song, String> songNameColumn = new TableColumn<>("Name");
        TableColumn<Song, String> songDurationColumn = new TableColumn<>("Duration");
        TableColumn<Song, String> songAlbumColumn = new TableColumn<>("Album");
        songsTableView.getColumns().addAll(songNameColumn, songDurationColumn, songAlbumColumn);

        Button addNewSongButton = new Button("Add song");
        Button editSongButton = new Button("Edit song");
        Button deleteSongButton = new Button("Delete song");

        songsData.addAll(group.getSongs());
        songsTableView.setItems(songsData);

        songNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        songAlbumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        songDurationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());

        songAlbumColumn.prefWidthProperty().bind(songsTableView.widthProperty().multiply(0.33));
        songNameColumn.prefWidthProperty().bind(songsTableView.widthProperty().multiply(0.33));
        songDurationColumn.prefWidthProperty().bind(songsTableView.widthProperty().multiply(0.33));

        Separator separatorMembers = new Separator();
        Separator separatorTours = new Separator();
        Separator separatorSongs = new Separator();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        gridPane.getColumnConstraints().add(columnConstraints);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(yearLabel, 0, 1);
        gridPane.add(genreLabel, 0, 2);
        gridPane.add(chartPositionLabel, 0, 3);

        gridPane.add(new Label("Members:"), 0, 4);
        gridPane.add(membersTableView, 0, 5);

        HBox hboxMembers = new HBox(addNewMemberButton,editMemberButton,deleteMemberButton);
        hboxMembers.setAlignment(Pos.CENTER);
        gridPane.add(hboxMembers,0,6);
        gridPane.add(separatorTours, 0, 7);

        gridPane.add(new Label("Tours:"), 0, 8);
        gridPane.add(toursTableView, 0, 9);

        HBox hboxTours = new HBox(addNewTourButton,exportToursReportButton,editTourButton,deleteTourButton);
        hboxTours.setAlignment(Pos.CENTER);
        gridPane.add(hboxTours,0,10);
        gridPane.add(separatorSongs, 0, 11);

        gridPane.add(new Label("Songs:"), 0, 12);
        gridPane.add(songsTableView, 0, 13);

        HBox hboxSongs = new HBox(addNewSongButton,editSongButton,deleteSongButton);
        hboxSongs.setAlignment(Pos.CENTER);
        gridPane.add(hboxSongs,0,14);

        GridPane.setColumnSpan(separatorMembers, 2);
        GridPane.setColumnSpan(separatorTours, 2);
        GridPane.setColumnSpan(separatorSongs, 2);
        VBox.setVgrow(songsTableView, Priority.ALWAYS);

        Scene scene = new Scene(gridPane, 700, 800);

        addNewMemberButton.setOnAction(event -> openAddMemberDialog(group, groupDetailsStage, membersData, membersTableView));
        addNewTourButton.setOnAction(event -> openAddTourDialog(group, groupDetailsStage, toursData, toursTableView));
        addNewSongButton.setOnAction(event -> openAddSongDialog(group, groupDetailsStage, songsData, songsTableView));

        editMemberButton.setOnAction(event -> openAddMemberDialog(group, groupDetailsStage, membersData, membersTableView));
        editTourButton.setOnAction(event -> openAddTourDialog(group, groupDetailsStage, toursData, toursTableView));
        editSongButton.setOnAction(event -> openAddSongDialog(group, groupDetailsStage, songsData, songsTableView));

        deleteMemberButton.setOnAction(event -> DataBaseHandler.deleteMember(group,membersTableView, membersData,"test"));
        deleteTourButton.setOnAction(event -> DataBaseHandler.deleteTour(group,toursTableView, toursData,"test"));
        deleteSongButton.setOnAction(event -> DataBaseHandler.deleteSong(group,songsTableView, songsData,"test"));

        exportToursReportButton.setOnAction(event -> PDFReporter.createReportTours(group.getId()));
        groupDetailsStage.setTitle("Band Information");
        groupDetailsStage.setScene(scene);
        groupDetailsStage.show();
    }

    private void openAddMemberDialog(Group group, Stage primaryStage, ObservableList<GroupMember> membersData, TableView<GroupMember> membersTableView) {
        GroupMember member = membersTableView.getSelectionModel().getSelectedItem();

        Map<String, String> parameters = new HashMap<>();
        if (member != null) {
            logger.info(member.getName());
            parameters.put("name", member.getName());
            parameters.put("role", member.getRole());
            parameters.put("age", member.getAge().toString());
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setPadding(new Insets(20));
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        TextField nameField;
        TextField roleField;
        TextField ageField;
        if (!parameters.isEmpty()) {
            nameField = new TextField(parameters.get("name"));
            roleField = new TextField(parameters.get("role"));
            ageField = new TextField(parameters.get("age"));
        }
        else {
            nameField = new TextField();
            roleField = new TextField();
            ageField = new TextField();
        }

        dialogGrid.add(new Label("Member name:"), 0, 0);
        dialogGrid.add(nameField, 1, 0);
        dialogGrid.add(new Label("Member role:"), 0, 1);
        dialogGrid.add(roleField, 1, 1);
        dialogGrid.add(new Label("Member age:"), 0, 2);
        dialogGrid.add(ageField, 1, 2);


        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                validateInputMember(nameField.getText(), roleField.getText(), ageField.getText());
                if (member != null) {
                    member.setName(nameField.getText());
                    member.setRole(roleField.getText());
                    member.setAge(Integer.valueOf(ageField.getText()));
                    Map<String, String> newValues = new HashMap<>();
                    newValues.put("name",nameField.getText());
                    newValues.put("role",roleField.getText());
                    newValues.put("age",ageField.getText());
                    DataBaseHandler.editDataMember(member.getId(),newValues,"test");
                }
                else {
                    GroupMember newMember = new GroupMember();
                    newMember.setName(nameField.getText());
                    newMember.setRole(roleField.getText());
                    newMember.setAge(Integer.valueOf(ageField.getText()));
                    DataBaseHandler.saveMemberToDB(group, newMember);
                }

                membersData.clear();
                membersData.addAll(group.getMembers());
                membersTableView.setItems(membersData);
                dialogStage.close();
            }
             catch (NumberFormatException nfe) {
                 AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, nfe.getMessage());
                 logger.warn(nfe.getMessage(),nfe);
             }
             catch (IllegalArgumentException lae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, "Fill all the fields!");
                logger.warn("Tried to add member, but some fields are empty");
            }
        });

        dialogGrid.add(addButton, 1, 3);

        GridPane.setHalignment(new Label("Member name:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("Member role:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("Member age:"), HPos.RIGHT);

        Scene dialogScene = new Scene(dialogGrid, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void openAddTourDialog(Group group, Stage primaryStage, ObservableList<Tour> toursData, TableView<Tour> toursTableView) {
        Tour tour = toursTableView.getSelectionModel().getSelectedItem();

        Map<String, String> parameters = new HashMap<>();
        if (tour != null) {
            parameters.put("name", tour.getName());
            parameters.put("begin", tour.getDateOfBeginning().toString());
            parameters.put("end", tour.getDateOfEnd().toString());
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setPadding(new Insets(20));
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        TextField nameField;
        DatePicker startDatePicker;
        DatePicker endDatePicker;

        if (!parameters.isEmpty()) {
            nameField = new TextField(parameters.get("name"));
            startDatePicker = new DatePicker(LocalDate.parse(parameters.get("begin")));
            endDatePicker = new DatePicker(LocalDate.parse(parameters.get("end")));
        }
        else {
            nameField = new TextField();
            startDatePicker = new DatePicker();
            endDatePicker = new DatePicker();
        }
        dialogGrid.add(new Label("Tour Name:"), 0, 0);
        dialogGrid.add(nameField, 1, 0);
        dialogGrid.add(new Label("Start Date:"), 0, 1);
        dialogGrid.add(startDatePicker, 1, 1);
        dialogGrid.add(new Label("End Date:"), 0, 2);
        dialogGrid.add(endDatePicker, 1, 2);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                validateInputTour(nameField.getText(), startDatePicker.getValue(), endDatePicker.getValue());
                if (tour != null) {
                    tour.setName(nameField.getText());
                    tour.setDateOfBeginning(startDatePicker.getValue());
                    tour.setDateOfEnd((endDatePicker.getValue()));
                    Map<String, String> newValues = new HashMap<>();
                    newValues.put("name",nameField.getText());
                    newValues.put("begin",startDatePicker.getValue().toString());
                    newValues.put("end",endDatePicker.getValue().toString());
                    DataBaseHandler.editDataTour(tour.getTourId(),newValues,"test");
                }
                else {
                    Tour newTour = new Tour();
                    newTour.setName(nameField.getText());
                    newTour.setDateOfBeginning(startDatePicker.getValue());
                    newTour.setDateOfEnd(endDatePicker.getValue());
                    DataBaseHandler.saveTourToDB(group, newTour);
                }
                toursData.clear();
                toursData.addAll(group.getTours());
                toursTableView.setItems(toursData);
                dialogStage.close();
            }
            catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, "Fill all the fields!");
                logger.warn("Tried to add tour, but some fields are empty");
            }
        });

        dialogGrid.add(addButton, 1, 3);

        GridPane.setHalignment(new Label("Concert Name:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("Start Date:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("End Date:"), HPos.RIGHT);

        Scene dialogScene = new Scene(dialogGrid, 350, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void openAddSongDialog(Group group, Stage primaryStage, ObservableList<Song> songsData, TableView<Song> songsTableView) {
        Song song = songsTableView.getSelectionModel().getSelectedItem();

        Map<String, String> parameters = new HashMap<>();
        if (song != null) {
            parameters.put("name", song.getName());
            parameters.put("duration", song.getDuration().toString());
            parameters.put("album", song.getAlbum());
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setPadding(new Insets(20));
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        TextField nameField;
        TextField durationField;
        TextField albumField;

        if (!parameters.isEmpty()) {
            nameField = new TextField(parameters.get("name"));
            durationField = new TextField((parameters.get("duration")));
            albumField = new TextField(parameters.get("album"));
        }
        else {
            nameField = new TextField();
            durationField = new TextField();
            albumField = new TextField();
        }

        dialogGrid.add(new Label("Song Name:"), 0, 0);
        dialogGrid.add(nameField, 1, 0);
        dialogGrid.add(new Label("Duration:"), 0, 1);
        dialogGrid.add(durationField, 1, 1);
        dialogGrid.add(new Label("Album:"), 0, 2);
        dialogGrid.add(albumField, 1, 2);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                validateInputSong(nameField.getText(), durationField.getText(), albumField.getText());
                if (song != null) {
                    song.setName(nameField.getText());
                    song.setDuration(Integer.valueOf(durationField.getText()));
                    song.setAlbum(albumField.getText());
                    Map<String, String> newValues = new HashMap<>();
                    newValues.put("name",nameField.getText());
                    newValues.put("duration",durationField.getText());
                    newValues.put("album",albumField.getText());
                    DataBaseHandler.editDataSong(song.getId(),newValues,"test");
                }
                else {
                    Song newSong = new Song();
                    newSong.setName(nameField.getText());
                    newSong.setDuration(Integer.valueOf(durationField.getText()));
                    newSong.setAlbum(albumField.getText());
                    DataBaseHandler.saveSongToDB(group, newSong);
                }
                songsData.clear();
                songsData.addAll(group.getSongs());
                songsTableView.setItems(songsData);
                dialogStage.close();
            }
            catch (NumberFormatException nfe) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, nfe.getMessage());
                logger.warn(nfe.getMessage());
            }
            catch (IllegalArgumentException iae) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR, "Error!", null, iae.getMessage());
                logger.warn(iae.getMessage(),iae);
            }
        });

        dialogGrid.add(addButton, 1, 3);

        GridPane.setHalignment(new Label("Song Name:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("Duration:"), HPos.RIGHT);
        GridPane.setHalignment(new Label("Album:"), HPos.RIGHT);

        Scene dialogScene = new Scene(dialogGrid, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
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

    public String validateInputGroup(String input, String fieldName) throws IllegalArgumentException, NumberFormatException {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Field " + fieldName + " is empty. Try again.");
        }
        if ((fieldName.equals("Year of foundation") || fieldName.equals("Place in chart")) && !input.matches("-?\\d+(\\.\\d+)?")) {
            throw new NumberFormatException("Field " + fieldName + " should be an integer!");
        }
        if ((fieldName.equals("Year of foundation") || fieldName.equals("Place in chart")) && Integer.parseInt(input) < 0) {
            throw new IllegalArgumentException("Field " + fieldName + " should be positive. Try again.");
        }
        return input;
    }

    public void validateInputMember(String name, String role, String age) throws IllegalArgumentException {
        if (name.isEmpty() || role.isEmpty() || age.isEmpty()) {
            throw new IllegalArgumentException("Field is empty. Try again.");
        }
        if ( !age.matches("-?\\d+(\\.\\d+)?")) {
            throw new NumberFormatException("Field age should be an integer!");
        }
        if (Integer.parseInt(age) < 0) {
            throw new IllegalArgumentException("Field age should be positive. Try again.");
        }
    }

    public void validateInputTour(String name, LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
        if (name.isEmpty() || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Field is empty. Try again.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date is after end date. Try again!");
        }
    }

    public void validateInputSong(String name, String duration , String album) throws IllegalArgumentException {
        if (name.isEmpty() || duration.isEmpty() || album.isEmpty()) {
            throw new IllegalArgumentException("Field is empty. Try again.");
        }
        if ( !duration.matches("-?\\d+(\\.\\d+)?")) {
            throw new NumberFormatException("Field duration should be an integer!");
        }
        if (Integer.parseInt(duration) < 0) {
            throw new IllegalArgumentException("Field duration should be positive integer");
        }
    }

    public static class IllegalArgumentException extends Exception {
        public IllegalArgumentException(String message) {
            super(message);
        }
    }
}
