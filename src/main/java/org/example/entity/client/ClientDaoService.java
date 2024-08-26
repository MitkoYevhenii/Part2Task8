package org.example.entity.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoService {
    private final Connection connection;

    private final PreparedStatement createSt;
    private final PreparedStatement selectMaxIdSt;
    private final PreparedStatement getByIdSt;
    private final PreparedStatement setNameSt;
    private final PreparedStatement deleteByIdSt;
    private final PreparedStatement listAllSt;

    public ClientDaoService(Connection connection) throws SQLException {
        this.connection = connection;

        createSt = connection.prepareStatement(
                "INSERT INTO client (name) VALUES(?)"
        );

        getByIdSt = connection.prepareStatement(
                "SELECT name FROM client WHERE id = ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM client"
        );

        setNameSt = connection.prepareStatement(
                "UPDATE client SET name = ? WHERE id = ?"
        );

        deleteByIdSt = connection.prepareStatement(
                "DELETE FROM client WHERE id = ?"
        );

        listAllSt = connection.prepareStatement(
                "SELECT id, name FROM client"
        );
    }

    public long create(String name) throws SQLException {
        validateName(name);

        createSt.setString(1, name);
        createSt.executeUpdate();

        long id;
        try (ResultSet resultSet = selectMaxIdSt.executeQuery()) {
            resultSet.next();
            id = resultSet.getLong("maxId");
        }

        return id;
    }

    public String getById(long id) throws SQLException {
        getByIdSt.setLong(1, id);

        try (ResultSet resultSet = getByIdSt.executeQuery()) {
            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getString("name");
        }
    }

    public void setName(long id, String name) throws SQLException {
        validateName(name);

        setNameSt.setString(1, name);
        setNameSt.setLong(2, id);

        int rowsUpdated = setNameSt.executeUpdate();
        if (rowsUpdated == 0) {
            throw new SQLException("Client not found");
        }
    }

    public void deleteById(long id) throws SQLException {
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }

    public List<Client> listAll() throws SQLException {
        try (ResultSet resultSet = listAllSt.executeQuery()) {
            List<Client> clients = new ArrayList<>();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                Client client = new Client();
                client.setId(id);
                client.setName(name);

                clients.add(client);
            }

            return clients;
        }
    }

    public void deleteAllWorker() throws SQLException {
        connection.prepareStatement(
                "DELETE FROM project_worker WHERE project_id IN (SELECT id FROM project WHERE client_id IN (SELECT id FROM client));\n" +
                        "DELETE FROM project WHERE client_id IN (SELECT id FROM client);\n" +
                        "DELETE FROM client;\n"
        ).executeUpdate();

    }

    private void validateName(String name) {
        if (name == null || name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Client name must be between 2 and 100 characters");
        }
    }
}
