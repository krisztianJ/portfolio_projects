package hu.unideb.inf.model;
/*Importok*/
import javax.persistence.EntityManager;/*JPA elemek az adatbázis kommunikációhoz*/
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/*VeradoDAO interfészt implementálja (a DAO célje elválasztani az adatkezelést a logikától)*/
public class JpaVeradoDAO implements VeradoDAO{


    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();



    /*Elment egy Verado objektumot az adatbázisba*/
    public  void saveVerado(Verado a) {
        entityManager.getTransaction().begin();
        entityManager.persist(a); /*Új rekordként menti el*/
        entityManager.getTransaction().commit();

    }

    /*Törli a megadott Verado objektumot az adatbázisból*/
    @Override
    public void deleteVerado(Verado a) {
        entityManager.getTransaction().begin();
        entityManager.remove(a);
        entityManager.getTransaction().commit();

    }

    /*Frissíti az adott véradó példány attribútumait*/
    @Override
    public void updateVerado(Verado a) {
        saveVerado(a);

    }

    /*Lekérdezi az összes Verado entitást az adatbázisból.*/
    @Override
    public List<Verado> getVeradok() {
        TypedQuery<Verado> query = entityManager.createQuery("SELECT a FROM Verado a", Verado.class);
        List<Verado> veradok = query.getResultList();
        return veradok;
    }

    /*Lekérdezi az összes Verado entitást az adatbázisból.*/
    @Override
    public void saveKorhaz(Korhaz korhaz) {
        entityManager.getTransaction().begin();
        entityManager.persist(korhaz);
        entityManager.getTransaction().commit();
    }

    /*Bezárja az adatbázis kapcsolatot és a factory-t.
    Ezt fontos hívni, ha végeztünk a DAO-val, különben memória- vagy kapcsolatproblémák lehetnek.
    * */
    @Override
    public void close() throws Exception {
        entityManager.close();
        entityManagerFactory.close();

    }
}
