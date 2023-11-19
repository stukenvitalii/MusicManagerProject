import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class DataBaseHandler {
    public static void saveGroupToDB(Group group, AppController appController) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

        System.out.println("Saving new band to DataBase");

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
        appController.initialize();

        AlertHandler.makeAlertWindow(Alert.AlertType.INFORMATION,"Success!",null,"Band " + group.getName() + " successfully added, " + "id is " + group.getId());
    }
    public static void getDataFromDB(String persistenceUnitName, List<Group> groups, ObservableList<Group> data) {
        try (EntityManager entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager()) {
            entityManager.getTransaction().begin();
            groups = entityManager.createQuery("from Group", Group.class).getResultList();
            data.clear();
            data.addAll(groups);
            entityManager.getTransaction().commit();
        }
    }
}
