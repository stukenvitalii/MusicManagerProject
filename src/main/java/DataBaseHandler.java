import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class DataBaseHandler {
    private static final Logger logger = LogManager.getLogger("mainLogger");
    public static void saveGroupToDB(Group group) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        logger.info("Saving new band to DataBase");

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Band " + group.getName() + " successfully added, " + "id is " + group.getId());
        logger.info("Group " + group.getName() + " successfully saved to DB");
    }

    public static void getDataFromDB(String persistenceUnitName, ObservableList<Group> data) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            logger.info("Trying to get data from DB");
            entityManager.getTransaction().begin();
            List<Group> groups = entityManager.createQuery("from Group", Group.class).getResultList();
            data.clear();
            data.addAll(groups);
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
    public static void editData(int selectedGroupId, Map<String,String> paramValue, String persistenceUnitName) {
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
}
