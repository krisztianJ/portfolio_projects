package hu.unideb.inf;

/*Importok*/
import java.sql.SQLException; /*SQL importok kivétel kezeléhez és naplózáshoz*/
import java.util.logging.Level;
import java.util.logging.Logger;

import hu.unideb.inf.model.*;
/*JAVAFX osztályok GUI-hoz stb*/
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/*A H2 adatbázis szerver vezérléséhez szükséges osztály.*/
import org.h2.tools.Server;

/*Alkalmazás belépési pontja, megörökli a javaFX Application osztályát*/
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        /*Betölti az FXML fájlt, amely a GUI kinézetét írja le. Az FXML fájl a resources/fxml/ mappában van.*/
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/FXMLStudentsScene.fxml"));

        /*Létrehoz egy Scene objektumot a betöltött FXML alapján.*/
        Scene scene = new Scene(loader.load());

        /*Ikonképet ad az ablakhoz*/
        Image image = new Image("/images/favicon.png");
                stage.getIcons().add(image);

                /*Címsor beállítása*/
        stage.setTitle("Véradós_alkalmazás");

        /*Ablak méret beállítása*/
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(800);

        /*majd ablak megjelenítése*/
        stage.show();
    }


    public static void main(String[] args) {
        /*Üzenet kiírása a konzolra*/
        System.out.println("alkalmazás elindult!");

        /*Elindítja a H2 adatbázist ha hiba van naplózza és kilép*/
        try {

            startDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        /*Elindítja a JavaFX alkalmazást*/
        launch(args);
        /*Ez meghívódik a launch() után, de gyakorlatban ez nem fog lefutni, mert a JavaFX app csak akkor tér vissza, ha teljesen leáll az alkalmazás*/
        stopDatabase();
    }

    /*Létrehoz egy Server példányt – ez az org.h2.tools.Server része, ami TCP és web hozzáférést ad az adatbázishoz.*/
    private static Server s = new Server();


    /*Elindítja az adatbázist, ha már fut akkor nem indítja el még egyszer*/
    private static void startDatabase() throws SQLException {
        s.runTool("-tcp", "-web", "-ifNotExists");
    }

    /*Leállítja az adatbázist*/
    private static void stopDatabase()  {
        s.shutdown();
    }
    
}
