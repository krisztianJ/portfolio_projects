package hu.unideb.inf.model;

import javax.persistence.*; /*A JPA annotációkhoz kell (@Entity, @Id, @GeneratedValue, stb.).*/
import java.util.ArrayList;
import java.util.List;

/*Ez egy JPA entitás osztály, amely a kórházakat (Korhaz) reprezentálja az adatbázisban.*/
@Entity /*A @Entity annotáció azt jelzi, hogy ez egy JPA entitás, vagyis egy táblának felel meg az adatbázisban.*/
public class Korhaz {



    @Id
    @GeneratedValue
    private int Id; /*Ez az elsődleges kulcs,automatikusan generált érték (@GeneratedValue), pl. AUTO_INCREMENT.*/
    /*Egyszerű oszlopok az adatbázisban.*/
    private String nev;
    private String ido;
    private boolean juttatas;
    private String helyszin;


    public Korhaz() {
    }
    public Korhaz(String helyszin, String ido, boolean juttatas, String nev) {

        this.helyszin=helyszin;
        this.ido=ido;
        this.juttatas=juttatas;
        this.nev=nev;
    }

    public boolean getJuttatas() {
        return juttatas;
    }

    public void setJuttatas(boolean juttatas) {
        this.juttatas = juttatas;
    }

    public String getHelyszin() {
        return helyszin;
    }

    public void setHelyszin(String helyszin) {
        this.helyszin = helyszin;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "korhaz")
    private List<Verado> veradok=new ArrayList<>();
/*Egy kórházhoz több véradó tartozhat (egy-a-többhöz kapcsolat).
@OneToMany: ez a kapcsolat típusa.
cascade = CascadeType.ALL: minden művelet (persist, remove, stb.) végrehajtódik a véradókon is.
@JoinColumn(name = "korhaz"): a Verado táblában lesz egy korhaz nevű idegen kulcs oszlop, ami erre az entitásra hivatkozik.*/

    /*Getterek, Setterek*/
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getNev() {
        return nev;
    }

    public String getIdo() {
        return ido;
    }

    public List<Verado> getVeradok() {
        return veradok;
    }



    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setIdo(String ido) {
        this.ido = ido;
    }

    public void setVeradok(List<Verado> veradok) {
        this.veradok = veradok;
    }

    @Override
    public String toString() {
        return this.Id + " " + this.nev + " "+ this.ido+ " "+ this.juttatas+ " "+ this.helyszin;
    }
}