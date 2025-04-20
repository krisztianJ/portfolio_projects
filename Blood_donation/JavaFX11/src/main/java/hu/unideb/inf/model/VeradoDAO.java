package hu.unideb.inf.model;

import java.util.List;

/*Az AutoCloseable interfész azt jelenti, hogy példányai automatikusan bezárhatók*/
public interface VeradoDAO extends AutoCloseable {

/*Előírja, hogy egy DAO-nak miket kell tudnia a Verado entitásokkal kapcsolatban.*/
    public void saveVerado(Verado a);

    public void deleteVerado(Verado a);
    public void updateVerado(Verado a);
    public List<Verado> getVeradok();
    public default void saveKorhaz(Korhaz korhaz){
        throw new UnsupportedOperationException();
    }

}