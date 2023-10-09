import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;

public class App{
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();

//        System.out.println("Start a hibernate test");
//
//        em.getTransaction().begin();
//        Group gr = new Group();
//        gr.setName("Pink Floyd");
//        gr.setYearOfFoundation(1980);
//        em.persist(gr);
//        em.getTransaction().commit();
//        System.out.println("New group ID is: " + gr.getId());


    }

}
