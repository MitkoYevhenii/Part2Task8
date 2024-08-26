package org.example.storage;

import org.example.prefs.Prefs;
import org.flywaydb.core.Flyway;

public class DatabaseService {

    public void initDatabase(String connectionUrl, String userName, String password) {
        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, userName, password)
                .load();
        flyway.migrate();
    }

    public void cleanDatabase(String connectionUrl, String userName, String password) {
        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, userName, password)
                .cleanDisabled(false) // Включаем возможность использовать clean
                .load();
        flyway.clean();
    }
}

