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

public class DataBaseHandler {
    private static final Logger logger = LogManager.getLogger("mainLogger");
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
    public static void deleteMember(TableView<GroupMember> membersTableView, ObservableList<GroupMember> membersData, String persistenceUnitName) {
        GroupMember selectedMember= membersTableView.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            int selectedMemberId = selectedMember.getId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete member");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM GroupMember WHERE id = ?1 ").setParameter(1, selectedMemberId).executeUpdate();
                entityManager.getTransaction().commit();
                membersData.remove(selectedMember);
                membersTableView.refresh();
            }
        }
        else {
            logger.info("Trying to delete member, but no member was selected");
        }

    }
    public static void deleteTour(TableView<Tour> toursTableView,ObservableList<Tour> toursData, String persistenceUnitName) {
        Tour selectedTour = toursTableView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            int selectedTourId = selectedTour.getTourId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete tour");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM Tour WHERE id = ?1 ").setParameter(1, selectedTourId).executeUpdate();
                entityManager.getTransaction().commit();
                toursData.remove(selectedTour);
                toursTableView.refresh();
            }
        }
        else {
            logger.warn("Trying to delete tour, but no tour was selected");
        }

    }
    public static void deleteSong(TableView<Song> songsTableView,ObservableList<Song> songsData, String persistenceUnitName) {
        Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            int selectedSongId = selectedSong.getId();
            try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
                logger.info("Trying to delete song");
                entityManager.getTransaction().begin();
                entityManager.createQuery("DELETE FROM Song WHERE id = ?1 ").setParameter(1, selectedSongId).executeUpdate();
                entityManager.getTransaction().commit();
                songsData.remove(selectedSong);
                songsTableView.refresh();
            }
        }
        else {
            logger.warn("Trying to delete song, but no song was selected");
        }

    }
}
