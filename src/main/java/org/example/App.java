package org.example;

import org.example.prefs.Prefs;
import org.example.storage.DatabaseService;
import org.example.storage.Storage;

public class App {
    public static void main(String[] args) {
        Prefs prefs = new Prefs();

        String dbUrl =  prefs.getString(Prefs.DB_JDBC_CONNECTION_URL);
        String dbUser = prefs.getString(Prefs.DB_JDBC_USERNAME);
        String dbPass = prefs.getString(Prefs.DB_JDBC_PASSWORD);

//        new DatabaseService().cleanDatabase(dbUrl, dbUser, dbPass);
        new DatabaseService().initDatabase(dbUrl, dbUser, dbPass);



    }
}

