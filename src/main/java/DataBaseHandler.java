import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
* @author stukenvitalii
* @since 01.12.2023
* @version 1.0
 */
public class DataBaseHandler {
    /** Logger object used for logging (Log4j2)*/
    private static final Logger logger = LogManager.getLogger("mainLogger");
    /**
     * Method used to save new group to database. Uses EntityManager to operate
    * @param group Group object that will be saved in database
     */
    public static void saveGroupToDB(Group group) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        try (EntityManager em = emf.createEntityManager()) {
            logger.info("Saving new band to DataBase");

            em.getTransaction().begin();
            em.persist(group);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Band " + group.getName() + " successfully added, " + "id is " + group.getId());
            logger.info("Group " + group.getName() + " successfully saved to DB");
        }
    }

    /**
     * Method used to save new member to database. Uses EntityManager to operate
     * @param group Group object which is "parent" for new GroupMember
     * @param member GroupMember object which will be saved to database
     */
    public static void saveMemberToDB(Group group, GroupMember member) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        try (EntityManager em = emf.createEntityManager()) {
            logger.info("Saving new member to DataBase");

            em.getTransaction().begin();

            group.getMembers().add(member);
            member.setGroup(group);
            em.persist(member);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Member " + member.getName() + " successfully added");
            logger.info("Member " + member.getName() + " successfully saved to DB");
        }
    }

    /**
     * Method used to save new song to database. Uses EntityManager to operate
     * @param group Group object which is "parent" for new Song
     * @param song Song object which will be saved to database
     */
    public static void saveSongToDB(Group group,Song song) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        try (EntityManager em = emf.createEntityManager()) {
            logger.info("Saving new song to DataBase");

            em.getTransaction().begin();
            group.getSongs().add(song);
            song.setGroup(group);
            em.persist(song);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Song " + song.getName() + " successfully added");
            logger.info("Song " + song.getName() + " successfully saved to DB");
        }
    }

    /**
     * Method used to save new tour to database. Uses EntityManager to operate
     * @param group Group object which is "parent" for new Tour
     * @param tour Tour object which will be saved to database
     */
    public static void saveTourToDB(Group group,Tour tour) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        try (EntityManager em = emf.createEntityManager()) {
            logger.info("Saving new tour to DataBase");

            em.getTransaction().begin();
            group.getTours().add(tour);
            tour.setGroup(group);
            em.persist(tour);
            em.getTransaction().commit();

            AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Tour " + tour.getName() + " successfully added");
            logger.info("Tour " + tour.getName() + " successfully saved to DB");
        }
    }

    /**
     * Method which gets all groups from database and fills groupsData. Uses EntityManager to operate
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     * @param groupsData Observable list which stores groups from database
     */
    public static void getDataFromDB(String persistenceUnitName, ObservableList<Group> groupsData) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            logger.info("Trying to get data from DB");
            entityManager.getTransaction().begin();
            List<Group> groups = entityManager.createQuery("from Group", Group.class).getResultList();
            groupsData.clear();
            groupsData.addAll(groups);

            entityManager.getTransaction().commit();
            logger.info("Fetching data from DB successful");
        }
    }

    /**
     * Method used to select specific data from database(searching). Uses simple SELECT-WHERE SQL query. \
     * Uses switch-case logic to work with different parameters
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     * @param parameter String that specifies parameter the parameter by which the search is performed
     * @param value Value of parameter
     * @param resultData Observable list that stores found groups
     */
    public static void selectDataFromDB(String persistenceUnitName, String parameter, String value, ObservableList<Group> resultData) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            logger.info("Trying to select specified data from DB");
            entityManager.getTransaction().begin();
            List<Group> groups;
            groups = switch (parameter) {
                case "group_name" ->
                        entityManager.createQuery("SELECT g from Group g WHERE LOWER(g.name) = LOWER(?1) ", Group.class).setParameter(1, value).getResultList();
                case "group_id" ->
                        entityManager.createQuery("SELECT g from Group g WHERE g.id = ?1 ", Group.class).setParameter(1, value).getResultList();
                case "group_year_of_foundation" ->
                        entityManager.createQuery("SELECT g from Group g WHERE g.yearOfFoundation =  ?1 ", Group.class).setParameter(1, value).getResultList();
                case "group_place_in_chart" ->
                        entityManager.createQuery("SELECT g from Group g WHERE g.placeInChart = ?1 ", Group.class).setParameter(1, value).getResultList();
                case "group_main_genre" ->
                        entityManager.createQuery("SELECT g from Group g WHERE LOWER(g.mainGenre) = LOWER(?1) ", Group.class).setParameter(1, value).getResultList();
                default -> null;
            };

            if (groups == null) {
                AlertHandler.makeAlertWindow(Alert.AlertType.ERROR,"Error",null,"Group with " + parameter + " = " + value + "doesn't exist!");
            }
            else {
                resultData.clear();
                resultData.addAll(groups);
            }
            entityManager.getTransaction().commit();
            logger.info("Selecting specified data from DB success");
        }
    }

    /**
     * Method that deletes group from database by given id
     * @param selectedId Id of group to be deleted
     * @param data Observable list that stores groups
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void deleteGroupById(int selectedId,ObservableList<Group> data,String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            logger.info("Trying to delete group");
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Group WHERE id = ?1 ").setParameter(1, selectedId).executeUpdate();

            Group selectedGroup = new Group();
            for (Group g:
                 data) {
                if (g.getId() == selectedId) {
                    selectedGroup = g;
                    break;
                }
            }
            data.remove(selectedGroup);
            entityManager.getTransaction().commit();
        }
    }

    /**
     * Method that updates current info about group. Uses simple UPDATE-WHERE SQL query with parameters
     * @param selectedGroupId Id of group to be edited
     * @param paramValue Map that stores parameter of a field and its value
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void editDataGroup(int selectedGroupId, Map<String,String> paramValue, String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Group SET name = ?1 , yearOfFoundation = ?2, mainGenre = ?3, placeInChart = ?4 WHERE id = ?5")
                    .setParameter(1, paramValue.get("name"))
                    .setParameter(2,paramValue.get("year"))
                    .setParameter(3,paramValue.get("genre"))
                    .setParameter(4,paramValue.get("place"))
                    .setParameter(5,selectedGroupId)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        }
    }
    /**
     * Method that updates current info about member. Uses simple UPDATE-WHERE SQL query with parameters
     * @param selectedMemberId Id of member to be edited
     * @param paramValue Map that stores parameter of a field and its value
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void editDataMember(int selectedMemberId, Map<String,String> paramValue, String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE GroupMember SET name = ?1 , role = ?2, age = ?3 WHERE id = ?4")
                    .setParameter(1, paramValue.get("name"))
                    .setParameter(2,paramValue.get("role"))
                    .setParameter(3,paramValue.get("age"))
                    .setParameter(4,selectedMemberId)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        }
    }
    /**
     * Method that updates current info about song. Uses simple UPDATE-WHERE SQL query with parameters
     * @param selectedSongId Id of song to be edited
     * @param paramValue Map that stores parameter of a field and its value
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void editDataSong(int selectedSongId, Map<String,String> paramValue, String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Song SET name = ?1 , duration = ?2, album = ?3 WHERE id = ?4")
                    .setParameter(1, paramValue.get("name"))
                    .setParameter(2,Integer.parseInt(paramValue.get("duration")))
                    .setParameter(3,paramValue.get("album"))
                    .setParameter(4,selectedSongId)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        }
    }
    /**
     * Method that updates current info about tour. Uses simple UPDATE-WHERE SQL query with parameters
     * @param selectedTourId Id of tour to be edited
     * @param paramValue Map that stores parameter of a field and its value
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void editDataTour(int selectedTourId, Map<String,String> paramValue, String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("UPDATE Tour SET name = ?1, dateOfBeginning = ?2, dateOfEnd = ?3 WHERE id = ?4")
                    .setParameter(1, paramValue.get("name"))
                    .setParameter(2, LocalDate.parse(paramValue.get("begin")))
                    .setParameter(3,LocalDate.parse(paramValue.get("end")))
                    .setParameter(4,selectedTourId)
                    .executeUpdate();
            entityManager.getTransaction().commit();
        }
    }

    /**
     * Method that deletes member from database. Uses DELETE-FROM SQL query.
     * @param group Group object which is "parent" for member to delete
     * @param membersTableView TableView which shows current members
     * @param membersData Observable list that stores current members
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void deleteMember(Group group,TableView<GroupMember> membersTableView, ObservableList<GroupMember> membersData, String persistenceUnitName) {
        GroupMember selectedMember= membersTableView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            int selectedMemberId = selectedMember.getId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete member");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM GroupMember WHERE id = ?1 ").setParameter(1, selectedMemberId).executeUpdate();
                entityManager.getTransaction().commit();
                membersData.remove(selectedMember);
                group.getMembers().remove(selectedMember);
                membersTableView.refresh();
            }
        }
        else {
            logger.info("Trying to delete member, but no member was selected");
        }
    }
    /**
     * Method that deletes tour from database. Uses DELETE-FROM SQL query.
     * @param group Group object which is "parent" for tour to delete
     * @param toursTableView TableView which shows current tours
     * @param toursData Observable list that stores current tours
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void deleteTour(Group group,TableView<Tour> toursTableView,ObservableList<Tour> toursData, String persistenceUnitName) {
        Tour selectedTour = toursTableView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            int selectedTourId = selectedTour.getTourId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete tour");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM Tour WHERE id = ?1 ").setParameter(1, selectedTourId).executeUpdate();
                entityManager.getTransaction().commit();
                toursData.remove(selectedTour);
                group.getTours().remove(selectedTour);
                toursTableView.refresh();
            }
        }
        else {
            logger.warn("Trying to delete tour, but no tour was selected");
        }

    }
    /**
     * Method that deletes song from database. Uses DELETE-FROM SQL query.
     * @param group Group object which is "parent" for song to delete
     * @param songsTableView TableView which shows current songs
     * @param songsData Observable list that stores current songs
     * @param persistenceUnitName Name of persistence unit in persistence.xml file
     */
    public static void deleteSong(Group group,TableView<Song> songsTableView,ObservableList<Song> songsData, String persistenceUnitName) {
        Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            int selectedSongId = selectedSong.getId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete song");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM Song WHERE id = ?1 ").setParameter(1, selectedSongId).executeUpdate();
                entityManager.getTransaction().commit();
                songsData.remove(selectedSong);
                group.getSongs().remove(selectedSong);
                songsTableView.refresh();
            }
        }
        else {
            logger.warn("Trying to delete song, but no song was selected");
        }
    }
}
