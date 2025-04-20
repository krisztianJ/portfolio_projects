package hu.unideb.inf.controller;

/*Importok*/
import javax.swing.*;
import java.sql.Connection;/*Adatbázis kapcsolathoz*/
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    /*Lokális H2 adatbázis a felhasználó home könyvtárában*/
    private String url = "jdbc:h2:~/db_jpa_fxml";
    private String user = "sa";
    private String password = "";

    /*Ez a metódus egy JDBC kapcsolatot ad vissza (vagy null-t, ha hiba történik)*/
    public Connection fileconnection()  {
        try {
            /*Betölti a H2 JDBC drivert.*/
            Class.forName("org.h2.Driver");
            /*Létrehoz egy új kapcsolatot a H2 adatbázissal.*/
            Connection connectdata = DriverManager.getConnection(url,user,password);
            return connectdata; /*Vissza adja a kapcsolatot*/
        }catch (ClassNotFoundException ex){/*Ha nem találja a drivert*/
            JOptionPane.showMessageDialog(null,"Error"+ex.getMessage());
            return null;
        } catch (SQLException ex) {/*Ha hiba van a kapcsolat létrehozásakor*/
            JOptionPane.showMessageDialog(null,"Error"+ex.getMessage());
            return null;
        }
    }
}
