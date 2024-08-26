package org.example.entity.worker;


import com.github.javafaker.Faker;
import org.example.entity.client.Client;
import org.example.entity.client.ClientDaoService;
import org.example.prefs.Prefs;
import org.example.storage.DatabaseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDaoServiceTest {
    Prefs prefs = new Prefs();
    private final String dbUrl = prefs.getString(Prefs.DB_JDBC_TEST_CONNECTION_URL).concat(";DB_CLOSE_DELAY=-1");
    private final String dbUser = prefs.getString(Prefs.DB_JDBC_USERNAME);
    private final String dbPass = prefs.getString(Prefs.DB_JDBC_PASSWORD);

    private ClientDaoService daoService;
    private Connection connection;
    private Faker faker;
    private Random random;


    @BeforeEach
    public void beforeEach() throws SQLException {
        new DatabaseService().initDatabase(dbUrl, dbUser, dbPass);
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        daoService = new ClientDaoService(connection);
        faker = new Faker();
        random = new Random();
    }


    @Test
    public void testClientCreateCorrectly() throws SQLException {
        List<Long> clientIdList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            // Создаем нового клиента с случайным именем
            Client client = new Client();
            String clientName = faker.name().fullName();
            client.setName(clientName);

            // Сохраняем клиента в базе данных и получаем его ID
            long clientId = daoService.create(clientName);
            clientIdList.add(clientId);

            // Проверяем, что клиент успешно сохранен и его имя совпадает с ожидаемым
            assertEquals(clientName, daoService.getById(clientId));
        }

        // Проверяем, что количество сохраненных клиентов совпадает с ожидаемым
        assertEquals(100, clientIdList.size());
    }


    @Test
    void testGetById() throws SQLException {
        String clientName = "Jane Doe";
        long clientId = daoService.create(clientName);

        String retrievedName = daoService.getById(clientId);
        assertEquals(clientName, retrievedName);
    }

    @Test
    void testSetName() throws SQLException {
        String clientName = "Initial Name";
        long clientId = daoService.create(clientName);

        String newName = "Updated Name";
        daoService.setName(clientId, newName);

        String updatedName = daoService.getById(clientId);
        assertEquals(newName, updatedName);
    }

    @Test
    void testDeleteById() throws SQLException {
        daoService.deleteAllWorker();
        String clientName = "TestName";
        long id = daoService.create(clientName);

        daoService.deleteById(id);

        Assertions.assertNull(daoService.getById(id));

    }

    @Test
    void testListAll() throws SQLException {
        daoService.deleteAllWorker();
        List<String> clientNames = Arrays.asList("Client 1", "Client 2", "Client 3");
        for (String name : clientNames) {
            daoService.create(name);
        }

        List<Client> clients = daoService.listAll();
        assertEquals(clientNames.size(), clients.size());

        for (int i = 0; i < clientNames.size(); i++) {
            assertEquals(clientNames.get(i), clients.get(i).getName());
        }
    }

    @Test
    void testCreateWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> daoService.create("A"));
        assertThrows(IllegalArgumentException.class, () -> daoService.create(""));
        assertThrows(IllegalArgumentException.class, () -> daoService.create(null));
    }

    @Test
    void testSetNameWithInvalidName() throws SQLException {
        String clientName = "Valid Name";
        long clientId = daoService.create(clientName);

        assertThrows(IllegalArgumentException.class, () -> daoService.setName(clientId, "A"));
        assertThrows(IllegalArgumentException.class, () -> daoService.setName(clientId, ""));
        assertThrows(IllegalArgumentException.class, () -> daoService.setName(clientId, null));
    }

    @AfterEach
    public void afterEach() throws SQLException {
        new DatabaseService().cleanDatabase(dbUrl, dbUser, dbPass);
        connection.close();
    }
}
