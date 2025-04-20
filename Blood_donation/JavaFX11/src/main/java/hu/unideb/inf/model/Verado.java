package hu.unideb.inf.model;

import javax.persistence.*;/*JPA entitás annotációkhoz.*/
import java.io.Serializable; /*Lehetővé teszi az objektum sorosítását (pl. adatátvitelhez vagy fájlba íráshoz).*/
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity /*@Entity: ez egy JPA entitás, tehát adatbázis-tábla reprezentáció.
implements Serializable: lehetővé teszi, hogy a Verado objektumokat fájlba lehessen menteni vagy hálózaton átküldeni.*/
public class Verado implements Serializable {

    @Id/*Az entitás elsődleges kulcsa.
AUTO: az adatbázis dönti el, hogyan generálja az ID-t (pl. autoincrement).*/
    @GeneratedValue(strategy = GenerationType.AUTO)
    /*Alapadatok*/
    private int id;
    private String nev;
    private String vercsoport;
    private int mennyiseg;
    private int KorhazID;

    public int getKorhazID() {
        return KorhazID;
    }

    public void setKorhazID(int korhazID) {
        KorhazID = korhazID;
    }
    //ArrayList<Verado> veradolista = new ArrayList<Verado>();

    /*Konstruktorok*/
    public Verado() {
    }

    /*Getterek, setterek*/
    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getVercsoport() {
        return vercsoport;
    }

    public int getMennyiseg() {
        return mennyiseg;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setVercsoport(String vercsoport) {
        this.vercsoport = vercsoport;
    }

    public void setMennyiseg(int mennyiseg) {
        this.mennyiseg = mennyiseg;
    }

    @Override
    public String toString() {
        return this.nev + " " + this.vercsoport + " "+ this.mennyiseg;
    }
}