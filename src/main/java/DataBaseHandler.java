import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.util.List;

public class DataBaseHandler {
    public static void saveGroupToDB(Group group) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        System.out.println("Saving new band to DataBase");

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION, "Success!", null, "Band " + group.getName() + " successfully added, " + "id is " + group.getId());
    }

    public static void getDataFromDB(String persistenceUnitName, ObservableList<Group> data) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            List<Group> groups = entityManager.createQuery("from Group", Group.class).getResultList();
            data.clear();
            data.addAll(groups);
            entityManager.getTransaction().commit();
        }
    }

    public static void selectDataFromDB(String persistenceUnitName, String parameter, String value, ObservableList<Group> resultData) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
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

            resultData.clear();
            assert groups != null;
            resultData.addAll(groups);
            entityManager.getTransaction().commit();
        }
    }
    public static void deleteGroupById(int selectedId,ObservableList<Group> data,String persistenceUnitName) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
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
}
