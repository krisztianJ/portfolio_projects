
package hu.unideb.inf.controller;

/*Importok*/
import hu.unideb.inf.model.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class FXMLVeradoSceneController implements Initializable {
    /*Példányosítások*/
    VeradoDAO aDAO = new JpaVeradoDAO();
    Korhaz korhaz = new Korhaz();
    static List<Korhaz> korhazLista = new ArrayList<>();
    static List<Verado> veradoLista= new ArrayList<>();



    ////////////////////////////////////////Függvények////////////////////////////////////////

    /*Van-e benne szám vagy sem ha van true egyébként false*/
    public Boolean CheckIfConNumb(String s){


        for (int i = 0; i < s.length(); i++){

            if( Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

/*Megnézi, hogy tartalmaz-e karaktert vagy sem*/
    public Boolean CheckIfConChar(String s){


        for (int i = 0; i < s.length(); i++){

            if(!Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public void InformPopUpWindow(String s){
        /*Létrehoz egy információ típusú figyelmeztető ablakot, amely megjeleníti az s stringet.*/
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        /* Megjeleníti a felugró ablakot (nem blokkoló, vagyis a program megy tovább).*/
        alert.show();
    }


    public void WarningPopUpWindow(String s){
        /*A popup sárga figyelmeztető ikonnal jelenik meg, ami jelzi, hogy valami nem ideális, de nem végzetes hiba.
        s- a megjelenítendő szöveg.
        * * */
        Alert alert = new Alert(Alert.AlertType.WARNING, s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }


    public static List<Korhaz> GetKorhaz() throws SQLException {

        /*Kapcsolat létrehozása*/
        ConnectionDB cn = new ConnectionDB();
        Connection cn1 = cn.fileconnection();

        /*SQL lekérdezés összeállítása és végrehajtása
        Kórháztáblából lekérdez mindent
        * * */
        String sql = "SELECT * FROM KORHAZ";
        Statement s = cn1.createStatement();
        ResultSet r = s.executeQuery(sql);

        /*Minden egyes sorhoz létrehoz egy Korhaz objektumot, feltölti mezőkkel, és hozzáadja a listához.*/
        while (r.next())
        {

            Korhaz kz = new Korhaz();
            kz.setId(r.getInt("Id"));
            kz.setNev(r.getString("nev"));
            kz.setJuttatas(r.getBoolean("juttatas"));
            kz.setIdo(r.getString("ido"));
            kz.setHelyszin(r.getString("helyszin"));
            korhazLista.add(kz);
        }

        return korhazLista;
    }


    public static List<Verado> GetVerado() throws SQLException {

        /*Kapcsolat létrehozása*/
        ConnectionDB cn = new ConnectionDB();
        Connection cn1 = cn.fileconnection();

        /*SQL parancs és végrehajtás*/
        String sql = "SELECT * FROM VERADO";
        Statement s = cn1.createStatement();
        ResultSet r = s.executeQuery(sql);

        /*Eredmények feldolgozása*/
        while (r.next())
        {
            Verado vr = new Verado();
            vr.setId(r.getInt("id"));
            vr.setNev(r.getString("nev"));
            vr.setMennyiseg(r.getInt("mennyiseg"));
            vr.setVercsoport(r.getString("vercsoport"));
            vr.setKorhazID(r.getInt("korhazid"));

            veradoLista.add(vr);
        }

        return veradoLista;
    }


    public void VPUpdateTableView(){
        /*Tábla oszlopok és adatlista inicializálása*/
        int i = 0;
        VeradoPontTabla.getItems().clear();
        /*A tábla kiürítése, hogy ne legyenek duplikált sorok*/
        VeradoPontAzonositoOszlop.setCellValueFactory(new PropertyValueFactory<>("Id"));
        VeradoPontNeveOszlop.setCellValueFactory(new PropertyValueFactory<>("helyszin"));
        VeradoPontNyitvatartasOszlop.setCellValueFactory(new PropertyValueFactory<>("ido"));
        VeradoPontJuttatasOszlop.setCellValueFactory(new PropertyValueFactory<>("juttatas"));
        VeradoPontHelyeOszlop.setCellValueFactory(new PropertyValueFactory<>("nev"));
        /*Ezek a TableColumn-ok hozzákötik a Korhaz osztály attribútumait a táblához.
Fontos: az oszlop neve (new PropertyValueFactory<>("...")) pontosan meg kell egyezzen a Korhaz osztály mezőneveivel / getterekkel.*/


        try {
            /*Adatbázis-lekérdezés*/
            ConnectionDB cn = new ConnectionDB();
            Connection cn1 = cn.fileconnection();
            /*Létrejön az SQL kapcsolat, lekérdezi az összes kórházat.*/
            String sql = "SELECT * FROM KORHAZ";
            Statement s = cn1.createStatement();
            ResultSet r = s.executeQuery(sql);
            /*Szűréshez használt alapértékek*/
            int nyitvatartasigora = 24;
            int nytvigmin = 59;
            int nyitvatartolora = 0;
            int nytvtolmin = 0;
            /*Feltétel nélküli betöltés*/
            if(VeradoPontKeresoField.getText().equals("") && VeradoPontNyitvatartasMinField.getText().equals("") && VeradoPontNyitvatartasMaxField.getText().equals("")) {
                while (i < korhazLista.size()) {
                    VeradoPontTabla.refresh();

                    Korhaz korhazseged = korhazLista.get(i);

                    korhazObservableList.add(korhazseged);
                    i++;
                }
                VeradoPontTabla.setItems(korhazObservableList);
            }
            else{
                if(!VeradoPontNyitvatartasMinField.getText().equals("")){
                    /*Időintervallum bekérése*/
                    if(CheckTimeConditionFormatum(VeradoPontNyitvatartasMinField.getText())){
                        String[] split = VeradoPontNyitvatartasMinField.getText().split(":");
                        nyitvatartolora = Integer.parseInt(split[0]);
                        nytvtolmin = Integer.parseInt(split[1]);
                    }

                }
                if(!VeradoPontNyitvatartasMaxField.getText().equals("")){
                    if(CheckTimeConditionFormatum(VeradoPontNyitvatartasMaxField.getText())){
                        String[] split = VeradoPontNyitvatartasMaxField.getText().split(":");
                        nyitvatartasigora = Integer.parseInt(split[0]);
                        nytvigmin = Integer.parseInt(split[1]);
                    }

                }
                if(!VeradoPontKeresoField.getText().equals("")){
                    /*Keresés név/helyszín/id alapján*/
                    for (int j = 0; j < korhazLista.size(); j++){
                        if (VeradoPontKeresoField.getText().equals(korhazLista.get(j).getNev()) &&
                                IdoConditiontol(korhazLista.get(j).getIdo(),nyitvatartolora,nytvtolmin,nyitvatartasigora,nytvigmin)) {

                            VeradoPontTabla.refresh();

                            Korhaz korhazseged = korhazLista.get(j);

                            korhazObservableList.add(korhazseged);

                        }
                        if (VeradoPontKeresoField.getText().equals(korhazLista.get(j).getHelyszin()) &&
                                IdoConditiontol(korhazLista.get(j).getIdo(),nyitvatartolora,nytvtolmin,nyitvatartasigora,nytvigmin)) {

                            VeradoPontTabla.refresh();

                            Korhaz korhazseged = korhazLista.get(j);

                            korhazObservableList.add(korhazseged);
                        }
                        else if ((VeradoPontKeresoField.getText()).equals(Integer.toString(korhazLista.get(j).getId())) &&
                                IdoConditiontol(korhazLista.get(j).getIdo(),nyitvatartolora,nytvtolmin,nyitvatartasigora,nytvigmin)) {

                            VeradoPontTabla.refresh();

                            Korhaz korhazseged = korhazLista.get(j);

                            korhazObservableList.add(korhazseged);
                        }
                    }
                }
                else if(!VeradoPontNyitvatartasMinField.getText().equals("") || !VeradoPontNyitvatartasMaxField.getText().equals("")){
                    for (int l = 0; l < korhazLista.size(); l++){
                        /*Ez egy saját metódusod, ami eldönti, hogy a kórház ido mezője beleesik-e a megadott időkeretbe*/
                        if(IdoConditiontol(korhazLista.get(l).getIdo(), nyitvatartolora, nytvtolmin, nyitvatartasigora,nytvigmin)){

                            VeradoPontTabla.refresh();

                            Korhaz korhazseged = korhazLista.get(l);

                            korhazObservableList.add(korhazseged);
                        }
                    }
                }

            }


        }catch (Exception e){

        }
    }



    public Boolean IdoConditiontol(String IdoToCheck, int tolora, int tolmin, int oraig, int minig){
        ArrayList<Integer> timelist = new ArrayList<>();
        /*Felbontja az IdoToCheck értéket óra–perc formában:*/
        String[] split=IdoToCheck.split("-");
        String[] hourminute = null;

        for (int i = 0; i < split.length; i++){
            hourminute = (split[i].split(":"));
            for (int k = 0; k < hourminute.length; k++) {
                timelist.add(Integer.parseInt(hourminute[k]));
            }
        }
        /*A nyitás és zárás óra beleesik a megadott intervallumba*/
        if(timelist.get(0) >= tolora && timelist.get(2) <= oraig){
            if(timelist.get(0) == tolora){

                if(timelist.get(1) >= tolmin ){

                    return true;
                }
                else
                {
                    return false;
                }
            }
            if(timelist.get(2) == oraig){

                if(timelist.get(3) <= minig){

                    return true;
                }
                else
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*A véradók táblázatának (VeradoTabla) frissítését végzi, és a szűrési logikát is tartalmazza*/
    public void VUpdateTableView(){
        int i = 0;
        VeradoTabla.getItems().clear();
        /*Kiüríti a táblát*/
        AzonositoOszlop.setCellValueFactory(new PropertyValueFactory<>("id"));
        NevOszlop.setCellValueFactory(new PropertyValueFactory<>("mennyiseg"));
        VercsoportOszlop.setCellValueFactory(new PropertyValueFactory<>("nev"));
        MennyisegOszlop.setCellValueFactory(new PropertyValueFactory<>("vercsoport"));
        KorhazIDOszlop.setCellValueFactory(new PropertyValueFactory<>("korhazID"));





        try {
            /*Adatbázis kapcsolat + lekérdezés*/
            ConnectionDB cn = new ConnectionDB();
            Connection cn1 = cn.fileconnection();

            String sql = "SELECT * FROM VERADO";
            Statement s = cn1.createStatement();
            ResultSet r = s.executeQuery(sql);
            if(VeradoKeresoField.getText().equals("")){

                /*Ha nincs keresési feltétel*/
                while (i < veradoLista.size()) {
                    VeradoTabla.refresh();

                    Verado veradoseged = veradoLista.get(i);

                    veradoObservableList.add(veradoseged);
                    i++;
                }
            }
            else {
                /*Ha van keresési feltétel*/
                veradoObservableList.clear();
                Boolean talalat = false;
                for (int j = 0; j < veradoLista.size(); j++) {
                    if ((VeradoKeresoField.getText()).equals(Integer.toString(veradoLista.get(j).getId()))) {

                        VeradoTabla.refresh();

                        Verado veradoseged = veradoLista.get(j);

                        veradoObservableList.add(veradoseged);
                        talalat = true;
                    }

                    if (VeradoKeresoField.getText().equals(veradoLista.get(j).getNev())) {

                        VeradoTabla.refresh();

                        Verado veradoseged = veradoLista.get(j);

                        veradoObservableList.add(veradoseged);
                        talalat = true;

                    } else if (VeradoKeresoField.getText().equals(veradoLista.get(j).getVercsoport())) {

                        VeradoTabla.refresh();

                        Verado veradoseged = veradoLista.get(j);

                        veradoObservableList.add(veradoseged);
                        talalat = true;


                } else if ((VeradoKeresoField.getText()).equals(Integer.toString(veradoLista.get(j).getMennyiseg()))) {

                    VeradoTabla.refresh();

                    Verado veradoseged = veradoLista.get(j);

                    veradoObservableList.add(veradoseged);
                    talalat = true;
                }
            }
                if(talalat == false) {
                    int korhazdid = 0;
                    for (int k = 0; k < korhazLista.size(); k++) {
                        if (VeradoKeresoField.getText().equals(korhazLista.get(k).getNev())) {
                            korhazdid =korhazLista.get(k).getId();
                            break;
                        }
                    }
                    for (int l = 0; l < veradoLista.size(); l++) {
                        if(veradoLista.get(l).getKorhazID() == korhazdid) {
                            VeradoTabla.refresh();

                            Verado veradoseged = veradoLista.get(l);

                            veradoObservableList.add(veradoseged);
                        }
                    }
                }
            }



        /*Vércsoport statisztika frissítése*/
            SetBloodCounters();
            VeradoTabla.setItems(veradoObservableList);
        }catch (Exception e){

        }


    }

    /*A vércsoportonkénti összesített vérmennyiséget számolja ki, majd az értékeket megjeleníti a GUI-n.*/
    public void SetBloodCounters(){
        /*Változók inicializálása*/
        int AP = 0,
                AM = 0,
                BP = 0,
                BM = 0,
                ABP = 0,
                ABM =0,
                OP = 0,
                OM = 0;
        /*Minden véradónál megnézi a vércsoportot, és az ahhoz tartozó változóhoz hozzáadja a mennyiséget*/
        for (int i = 0; i < veradoLista.size(); i++) {
            if (veradoLista.get(i).getVercsoport().equals("A+")) AP += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("A-")) AM += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("B+")) BP += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("B-")) BM += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("AB+")) ABP += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("AB-")) ABM += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("0+")) OP += veradoLista.get(i).getMennyiseg();
            if (veradoLista.get(i).getVercsoport().equals("0-")) OM += veradoLista.get(i).getMennyiseg();
        }
        /*Az adott vércsoporthoz tartozó szövegmező értékét frissíti*/
        APlusCounter.setText(String.valueOf(AP));
        AMCounter.setText(String.valueOf(AM));
        BPCounter.setText(String.valueOf(BP));
        BMCounter.setText(String.valueOf(BM));
        ABPCounter.setText(String.valueOf(ABP));
        ABMCounter.setText(String.valueOf(ABM));
        OPCounter.setText(String.valueOf(OP));
        OMCounter.setText(String.valueOf(OM));

    }

    /*Arra szolgál, hogy ellenőrizze, hogy a megadott nyitvatartási idő helyes formátumban van-e, és hogy a kezdő időpont kisebb-e, mint a záró.*/
    public boolean CheckTimeFormatum(String s){

        /*Csak akkor folytatja, ha pontosan "HH:mm-HH:mm" formátumot lát.*/
        if(s.length()==11) {
            if (s.charAt(2) == ':' && s.charAt(5) == '-' && s.charAt(8) == ':') {
                ArrayList<String> timelist = new ArrayList<>();
                /*Óra–perc mezők feldarabolása*/
                String[] split=s.split("-");
                String[] hourminute = null;
                for (int i = 0; i < split.length; i++){
                    hourminute = (split[i].split(":"));
                    for (int k = 0; k < hourminute.length; k++) {
                        timelist.add(hourminute[k]);
                    }
                }


                for (int i = 0; i < timelist.size(); i++){
                    /*Ellenőrzi, hogy minden rész szám-e*/
                    if (CheckIfConChar(timelist.get(i))){
                        WarningPopUpWindow("Hibás nyitvatartás formátum!(ÓÓ:mm-ÓÓ:mm)");
                        return false;

                    }
                    else {
                        for (int j = 0; j < timelist.size(); j++) {

                        }
                        int MinOra = Integer.parseInt(timelist.get(0));
                        int MinPerc = Integer.parseInt(timelist.get(1));
                        int MaxOra = Integer.parseInt(timelist.get(2));
                        int MaxPerc = Integer.parseInt(timelist.get(3));
                        /*Értéktartomány ellenőrzés*/
                        if ((MinOra >= 0 && MinOra <= 24 && MinPerc >= 0 && MinPerc < 60) && (MaxOra >= 0 && MaxOra <= 24 && MaxPerc >= 0 && MaxPerc < 60)) {
                            if (MinOra < MaxOra) {

                                return true;

                    /*Hiba esetén figyelmeztető ablakot dob*/
                            } else {
                                WarningPopUpWindow("Hibás nyitvatartás formátum!A nyitás nem lehet nagyobb mint a zárás!");
                                return false;
                            }
                        } else {
                            WarningPopUpWindow("Hibás nyitvatartás formátum!");

                            return false;
                        }
                    }

                }


            }
            else{
                WarningPopUpWindow("Ügyelj arra hogy megfelelő formátumban add meg a nyitvatartást! (óó:pp-óó:pp");
                return false;
            }
        }else{
            WarningPopUpWindow("Ügyelj arra hogy megfelelő formátumban add meg a nyitvatartást! (óó:pp-óó:pp");
            return false;
        }
        return false;

    }

    /*Egyetlen időpont (HH:mm formátumú) helyességét ellenőrzi.*/
    public boolean CheckTimeConditionFormatum(String s){

        /*Hossz és formátum ellenőrzése*/
        if(s.length()==5) {
            if (s.charAt(2) == ':') {

                /*Óra és perc különválasztása*/
                String[] split = s.split(":");
                String ora = split[0];
                String perc = split[1];


                /*Óra és perc különválasztása*/
                if (CheckIfConChar(ora)) {
                    WarningPopUpWindow("Hibás nyitvatartás formátum!(ÓÓ:mm-ÓÓ:mm)");
                    return false;

                } else if (CheckIfConChar(perc)) {
                    WarningPopUpWindow("Hibás nyitvatartás formátum!(ÓÓ:mm-ÓÓ:mm)");
                    return false;

                } else {

                    int MinOra = Integer.parseInt(ora);
                    int MinPerc = Integer.parseInt(perc);

                    /*Tartományellenőrzés*/
                    if (MinOra >= 0 && MinOra <= 24 && MinPerc >= 0 && MinPerc < 60) {


                        return true;

                        /*Hiba esetén popup*/
                    } else {
                        WarningPopUpWindow("Hibás nyitvatartás formátum!");

                        return false;
                    }
                }


            }
            else{
                WarningPopUpWindow("Ügyelj arra hogy megfelelő formátumban add meg a nyitvatartást! (óó:pp-óó:pp");
                return false;
            }
        }
        else{
            WarningPopUpWindow("Ügyelj arra hogy megfelelő formátumban add meg a nyitvatartást! (óó:pp-óó:pp");
            return false;
        }


    }
    ////////////////////////////////////////FÜGGVÉNYEK VÉGE////////////////////////////////////////



////////////////////////////////////////Veradas Tab////////////////////////////////////////


//BUTTON INICIALIZALAS

    @FXML
    private Button myButton;
    @FXML
    private Button myTorolButton;
    @FXML
    private Button myKeresButton;
    @FXML
    private Button updateButton1;


    //BUTTON INICIALIZALAS


    //VERADO TABLA

    @FXML
    private TableView<Verado> VeradoTabla;

    @FXML
    private TableColumn<Verado, Integer> AzonositoOszlop;

    @FXML
    private TableColumn<Verado, Integer> KorhazIDOszlop;

    @FXML
    private TableColumn<Verado, String> MennyisegOszlop;

    @FXML
    private TableColumn<Verado, String> NevOszlop;

    @FXML
    private TableColumn<Verado, String> VercsoportOszlop;
    ObservableList<Verado> veradoObservableList = FXCollections.observableArrayList();

//VERADO TABLA VEGE


    //VERADO BUTTONOK

    @FXML
    void VeradoHozzaadButtonPushed(ActionEvent event) throws SQLException {
        /*JPA beállítás*/
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        /*Ez alapján ellenőrzi, hogy az input vércsoport érvényes-e*/
        String[] VercsoportTomb = {"A+", "A-","B+","B-","AB+","AB-","0+","0-"};

        /*Validációs flagek, ezek figyelik, hogy minden adat rendben van-e a mentés előtt.*/
        Boolean mezoures = false;
        Boolean vercsoportcontain = false;
        Boolean vermennyiseg = false;
        Boolean korhazletezik = false;

        /*Ha bármelyik mező üres, figyelmeztető popup jön*/
        if(VeradoMennyisegField.getText().equals("") ||
                VeradoNevField.getText().equals("") ||
                VeradoVercsoportField.getText().equals("") ||
                korhazHozzaField.getText().equals(""))
        {
            WarningPopUpWindow("Hiba! Ne hagyj üres mezőt.");
            mezoures = true;
        }

        else {
            if (CheckIfConChar(VeradoMennyisegField.getText())){
                WarningPopUpWindow("Hiba! Csak számot adj meg mennyiségnek.");

            }
            if(Integer.parseInt(VeradoMennyisegField.getText()) > 500 || Integer.parseInt(VeradoMennyisegField.getText()) < 450){
                WarningPopUpWindow("Hiba! A mennyiség 450 és 500 ml között legyen.");
            }
            else{
                vermennyiseg = true;
            }
            for (int i = 0; i < VercsoportTomb.length; i++) {
                if (VercsoportTomb[i].equals(VeradoVercsoportField.getText())) {
                    vercsoportcontain = true;
                    break;
                }
            }

            if (vercsoportcontain == false) {
                WarningPopUpWindow("Hiba! Nem megfelelő vércsoport formátum.");
            }

            for (int i = 0; i < VeradoNevField.getText().length(); i++) {

                if (CheckIfConNumb(VeradoNevField.getText())) {
                    WarningPopUpWindow("Hiba! Rossz név formátum.");
                    break;
                }

            }
            for (int b= 0; b < korhazLista.size(); b++)
            {

                if(korhazHozzaField.getText().equals(korhazLista.get(b).getNev()))
                {
                    korhazletezik = true;

                }

            }
            if(korhazletezik == false){
                WarningPopUpWindow("Nincs ilyen korház az adatbázisban!");
            }
        }

        if (vermennyiseg == true &&
                mezoures == false &&
                vercsoportcontain == true &&
                CheckIfConNumb(VeradoNevField.getText()) == false &&
                korhazletezik == true) {

            Verado s = new Verado();
            s.setNev(VeradoNevField.getText());
            s.setVercsoport(VeradoVercsoportField.getText());
            s.setMennyiseg(Integer.parseInt(VeradoMennyisegField.getText()));

            for (int i = 0; i < korhazLista.size(); i++)
            {
                System.out.println(korhazHozzaField.getText());
                if(korhazLista.get(i).getNev().equals(korhazHozzaField.getText()))
                {
                    s.setKorhazID(korhazLista.get(i).getId());
                    System.out.println(s.getKorhazID());
                }
            }

            /* Mentés + lista frissítés*/
            veradoLista.add(s);
            aDAO.saveVerado(s);

            InformPopUpWindow("Sikeres adatfelvétel!");

            if(korhazHozzaField.getText().equals("")||korhazHozzaField.getText().isEmpty()==true){
                VUpdateTableView();
            }
            else{
                VUpdateTableView();
            }
        }

    }


    @FXML
    void VeradoKeresButtonPushed(ActionEvent event) {

        VUpdateTableView();

    }


    @FXML
    void VeradoTorolButtonPushed(ActionEvent event) throws SQLException {
        /*Létrehoz egy kapcsolatot a H2 adatbázishoz*/
        ConnectionDB cn = new ConnectionDB();
        Connection cn1 = cn.fileconnection();

        /*Adatok lekérése a TableView-ból*/
        ObservableList<Verado> allVerado,singleVerado;
        allVerado = VeradoTabla.getItems();

        /*Kijelöli a sort*/
        singleVerado = VeradoTabla.getSelectionModel().getSelectedItems();

        /*Kitörli a kiválasztott sort a JavaFX táblázatból az adatbázis törlése előtt*/
        singleVerado.forEach(allVerado::remove);
        System.out.println(VeradoTabla.getSelectionModel().getSelectedItem());

        /*Lekéri a törlendő rekord azonosítóját*/
        int ID = VeradoTabla.getSelectionModel().getSelectedItem().getId();
        /*Törli a rekordot az adatbázisból*/
        PreparedStatement statement = cn1.prepareStatement("DELETE FROM VERADO WHERE ID ="+ID+";");

        statement.execute();
        veradoLista.clear();
        try {
            veradoLista = GetVerado();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        VUpdateTableView();
    }

    @FXML
    void VeradoUpdateButtonPushed(ActionEvent event) throws SQLException{

        /*Érvényes vércsoportok listája*/
        String[] VercsoportTomb = {"A+", "A-","B+","B-","AB+","AB-","0+","0-"};

        /*Segítik követni, hogy minden mező helyes-e a frissítés előtt*/
        Boolean mezoures = false;
        Boolean vercsoportcontain = false;
        Boolean vermennyiseg = false;

        /*Ha bármelyik mező üres, figyelmeztető ablak jelenik meg*/
        if(VeradoMennyisegFieldUpdate1.getText().equals("") ||
                VeradoNevFieldUpdate1.getText().equals("") ||
                VeradoVercsoportFieldUpdate1.getText().equals("") ||
                korhazHozzaFieldUpdate1.getText().equals(""))
        {
            WarningPopUpWindow("Hiba! Ne hagyj üres mezőt.");
            mezoures = true;
        }
        else {
            if (CheckIfConChar(VeradoMennyisegFieldUpdate1.getText())){
                WarningPopUpWindow("Hiba! Csak számot adj meg mennyiségnek.");

            }
            if(Integer.parseInt(VeradoMennyisegField.getText()) > 500 || Integer.parseInt(VeradoMennyisegField.getText()) < 450){
                WarningPopUpWindow("Hiba! A mennyiség 450 és 500 ml között legyen.");
            }
            else{
                vermennyiseg = true;
            }
            for (int i = 0; i < VercsoportTomb.length; i++) {
                if (VercsoportTomb[i].equals(VeradoVercsoportFieldUpdate1.getText())) {
                    vercsoportcontain = true;
                    break;
                }
            }

            if (vercsoportcontain == false) {
                WarningPopUpWindow("Hiba! Nem megfelelő vércsoport formátum.");
            }

            for (int i = 0; i < VeradoNevFieldUpdate1.getText().length(); i++) {

                if (CheckIfConNumb(VeradoNevFieldUpdate1.getText())) {
                    WarningPopUpWindow("Hiba! Rossz név formátum.");
                    break;
                }

            }
        }
        if (CheckIfConChar(VeradoMennyisegFieldUpdate1.getText()) == false &&
                mezoures == false &&
                vercsoportcontain == true &&
                CheckIfConNumb(VeradoNevFieldUpdate1.getText()) == false &&
                vermennyiseg == true) {

            ConnectionDB ucn = new ConnectionDB();
            Connection ucn1 = ucn.fileconnection();
            ObservableList<Verado> allUpdateVerado, singleUpdateVerado;
            allUpdateVerado = VeradoTabla.getItems();

            singleUpdateVerado = VeradoTabla.getSelectionModel().getSelectedItems();

            String segedID = "";
            int ID = VeradoTabla.getSelectionModel().getSelectedItem().getId();
            /*Kórház ID lekérése*/
            for (int i = 0; i < korhazLista.size(); i++) {
                System.out.println(korhazHozzaField.getText());
                if (korhazLista.get(i).getNev().equals(korhazHozzaFieldUpdate1.getText())) {
                    segedID = korhazLista.get(i).getId() + "";


                }
            }
            /*SQL UPDATE művelet*/
            PreparedStatement statement = ucn1.prepareStatement("UPDATE VERADO SET NEV='" + VeradoNevFieldUpdate1.getText() + "',KORHAZID='" + segedID + "',MENNYISEG='" + VeradoMennyisegFieldUpdate1.getText() + "',VERCSOPORT='" + VeradoVercsoportFieldUpdate1.getText() + "' WHERE ID=" + ID + ";");
            statement.execute();
            /*Lista frissítés + tábla újratöltés*/
            veradoLista.clear();
            try {
                veradoLista = GetVerado();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            VUpdateTableView();


        }
    }

//VERADO BUTTONOK VEGE




//VERCSOPORT BOOLEAN VEGE


    //VER MENNYISEG
    @FXML
    private TextField VeradoMennyisegMax;

    @FXML
    private TextField VeradoMennyisegMin;
    //VER MENNYISEG VEGE


    //VERADO TEXTFIELD
    @FXML
    private TextField VeradoNevField;
    @FXML
    private TextField VeradoMennyisegField;
    @FXML
    private TextField VeradoKeresoField;
    @FXML
    private TextField VeradoVercsoportField;
    @FXML
    private TextField VeradoNevFieldUpdate1;
    @FXML
    private TextField VeradoVercsoportFieldUpdate1;
    @FXML
    private TextField VeradoMennyisegFieldUpdate1;
    @FXML
    private TextField korhazHozzaFieldUpdate1;




////////////////////////////////////////Verado pont Tab///////////////////////////////////////



//VERADOPONT TABLA

    @FXML
    private TableView<Korhaz> VeradoPontTabla;
    @FXML
    private TableColumn<Korhaz, Integer> VeradoPontAzonositoOszlop;
    @FXML
    private TableColumn<Korhaz, String> VeradoPontNyitvatartasOszlop;
    @FXML
    private TableColumn<Korhaz, String> VeradoPontNeveOszlop;
    @FXML
    private TableColumn<Korhaz, String> VeradoPontHelyeOszlop;
    @FXML
    private TableColumn<Korhaz, Boolean> VeradoPontJuttatasOszlop;
    ObservableList<Korhaz> korhazObservableList = FXCollections.observableArrayList();

    //VERADOPONT TABLA VEGE



    //VERADOPONT TEXTFIELD

    @FXML
    private TextField VeradoPontNyitvatartasField;
    @FXML
    private TextField VeradoPontHelyField;
    @FXML
    private TextField VeradoPontKeresoField;
    @FXML
    private TextField VeradoPontJuttatasField;
    @FXML
    private TextField VeradoPontNeveField;
    @FXML
    private TextField VeradoPontNeveFieldUpdate1;
    @FXML
    private TextField VeradoPontJuttatasFieldUpdate1;
    @FXML
    private TextField VeradoPontNyitvatartasFieldUpdate1;
    @FXML
    private TextField VeradoPontHelyFieldupdate1;

    //VERADOPONT TEXTFIELD VEGE



    //NYITVATARTAS

    @FXML
    private TextField VeradoPontNyitvatartasMinField;
    @FXML
    private TextField VeradoPontNyitvatartasMaxField;

    //NYITVATARTAS



    //VERADOPONT BUTTON
    @FXML
    private Text APlusCounter;
    @FXML
    private Text AMCounter;
    @FXML
    private Text BPCounter;
    @FXML
    private Text BMCounter;
    @FXML
    private Text ABPCounter;
    @FXML
    private Text ABMCounter;
    @FXML
    private Text OPCounter;
    @FXML
    private Text OMCounter;





    @FXML
    private Button VeradoPontHButton;

    @FXML
    private Button VeradoPontHButton1;
    @FXML
    private Button VeradoPontHButtonUpdate1;

    @FXML
    private  TextField korhazHozzaField;


/*Felelős egy új véradó pont (kórház) hozzáadásáért az alkalmazásban*/
    @FXML
    void VeradoPontHozzaadButtonPushed(ActionEvent event) throws SQLException {
        /*JPA beállítás*/
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        Boolean seged=null;

        /*Ezek figyelik a mezők helyességét és a juttatás mező értékét*/
        Boolean mezoureskorhaz = false;
        Boolean nyitv = false;
        Boolean juttatasformat = false;
        Boolean juttatascheck = false;
        /*Üres mezők ellenőrzése*/
        if(VeradoPontNeveField.getText().equals("")||
                VeradoPontJuttatasField.getText().equals("")||
                VeradoPontNyitvatartasField.getText().equals("")||
                VeradoPontHelyField.getText().equals("")
        ){
            WarningPopUpWindow("Hiba! Ne hagyj ures mezot!");
            mezoureskorhaz=true;

        }
        else{
        /*Ez ellenőrzi, hogy a HH:mm-HH:mm formátum érvényes-e*/
            nyitv = CheckTimeFormatum(VeradoPontNyitvatartasField.getText());

        }
        if (!VeradoPontJuttatasField.getText().equals("")) {
            /*Juttatás érték ellenőrzése*/
            if (VeradoPontJuttatasField.getText().equals("VAN") || VeradoPontJuttatasField.getText().equals("NINCS")) {
                if (VeradoPontJuttatasField.getText().equals("VAN")) {
                    juttatascheck = true;
                } else {
                    juttatascheck = false;
                }
                juttatasformat = true;
            } else {
                WarningPopUpWindow("Hibás juttatás formátum!(VAN vagy NINCS)");
            }
        }

        if(mezoureskorhaz==false&&nyitv==true && juttatasformat == true) {

        /*Ha minden oké → új kórház létrehozása*/
            Korhaz korhaz = new Korhaz();
            korhaz.setNev(VeradoPontNeveField.getText());
            korhaz.setIdo(VeradoPontNyitvatartasField.getText());
            korhaz.setJuttatas(juttatascheck);
            korhaz.setHelyszin(VeradoPontHelyField.getText());

            korhazLista.add(korhaz);

            /*Majd elmenti adatbázisba*/
            aDAO.saveKorhaz(korhaz);

            /*Majd elmenti adatbázisba:*/
            InformPopUpWindow("Sikeres adatátvitel!");
            VPUpdateTableView();

        }
    }


    @FXML
    void VeradoPontKeresButtonPushed(ActionEvent event) {
        VPUpdateTableView();
    }

    /*A kijelölt véradó pont (kórház) törléséért felel a JavaFX alkalmazásban*/
    @FXML
    void VeradoPontTorolButtonPushed(ActionEvent event) throws SQLException {
        /* Megnyit egy adatbáziskapcsolatot*/
        ConnectionDB cn = new ConnectionDB();
        Connection cn1 = cn.fileconnection();

        /*Kiválasztott elem(ek) lekérése és eltávolítása a táblából*/
        ObservableList<Korhaz> allKorhaz,singleKorhaz;
        allKorhaz = VeradoPontTabla.getItems();
        singleKorhaz = VeradoPontTabla.getSelectionModel().getSelectedItems();
        singleKorhaz.forEach(allKorhaz::remove);

        /*Az ID alapján törli az adatbázisból a kórházat*/
        int ID = VeradoPontTabla.getSelectionModel().getSelectedItem().getId();
        PreparedStatement statement = cn1.prepareStatement("DELETE FROM KORHAZ WHERE ID ="+ID+";");

        /*Az összes kórház újratöltése adatbázisból, majd frissítés a táblázatban*/
        statement.execute();
        korhazLista.clear();
        try {
            korhazLista = GetKorhaz();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        VPUpdateTableView();
    }

    /*Felelős a kijelölt véradó pont (kórház) adatainak frissítéséért (név, nyitvatartás, juttatás, helyszín) a JavaFX alkalmazásban*/
    @FXML
    void VeradoPontHozzaadUpdateButtonPushed(ActionEvent event)throws SQLException{
        /*Ezek a változók segítik követni, hogy a mezők rendben vannak-e.*/
        Boolean mezoureskorhaz = false;
        Boolean nyitv = false;
        Boolean juttatascheck = false;
        Boolean juttatasformat = false;

        /*Üres mezők ellenőrzése*/
        if(VeradoPontNeveFieldUpdate1.getText().equals("")||
                VeradoPontJuttatasFieldUpdate1.getText().equals("")||
                VeradoPontNyitvatartasFieldUpdate1.getText().equals("")||
                VeradoPontHelyFieldupdate1.getText().equals("")
        ){
            WarningPopUpWindow("Hiba! Ne hagyj ures mezot!");
            mezoureskorhaz=true;

        }
        else{
            /*Ez ellenőrzi, hogy csak "VAN" vagy "NINCS" szerepeljen a juttatás mezőben*/
            if (!VeradoPontJuttatasFieldUpdate1.getText().equals("")) {
                if (VeradoPontJuttatasFieldUpdate1.getText().equals("VAN") || VeradoPontJuttatasFieldUpdate1.getText().equals("NINCS")) {
                    if (VeradoPontJuttatasFieldUpdate1.getText().equals("VAN")) {
                        juttatascheck = true;
                    } else {
                        juttatascheck = false;
                    }
                    juttatasformat = true;
                } else {
                    WarningPopUpWindow("Hibás juttatás formátum!(VAN vagy NINCS)");
                }
            }
        /*Ellenőrzi, hogy a nyitvatartás HH:mm-HH:mm formátumú és időben helyes-e*/
            nyitv = CheckTimeFormatum(VeradoPontNyitvatartasFieldUpdate1.getText());
        }
        if(mezoureskorhaz==false&&nyitv==true && juttatasformat == true) {

            /*Feltételek teljesülése esetén → SQL UPDATE*/
            ConnectionDB ucn = new ConnectionDB();
            Connection ucn1 = ucn.fileconnection();
            ObservableList<Korhaz> allUpdateVerado, singleUpdateVerado;
            allUpdateVerado = VeradoPontTabla.getItems();

            singleUpdateVerado = VeradoPontTabla.getSelectionModel().getSelectedItems();
            String segedID = "";
            int ID = VeradoPontTabla.getSelectionModel().getSelectedItem().getId();
            Boolean segedJuttatas=false;
            if(VeradoPontJuttatasFieldUpdate1.getText().equals("true")){
                segedJuttatas=true;
            }
            else{
                segedJuttatas=false;
            }
            PreparedStatement statement = ucn1.prepareStatement("UPDATE KORHAZ SET HELYSZIN='" + VeradoPontHelyFieldupdate1.getText() + "',IDO='" + VeradoPontNyitvatartasFieldUpdate1.getText() + "',JUTTATAS='" + segedJuttatas + "',NEV='" + VeradoPontNeveFieldUpdate1.getText() + "' WHERE ID=" + ID + ";");
            /*Lista frissítése*/
            statement.execute();
            korhazLista.clear();
            try {
                korhazLista = GetKorhaz();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            VPUpdateTableView();
        }
    }

//VERADOPONT GOMBOK VEGE


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Véradók betöltése adatbázisból*/
        try {
            veradoLista = GetVerado();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VUpdateTableView();
        /*Kórházak betöltése*/
        try {
            korhazLista = GetKorhaz();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VPUpdateTableView();

    }
}


