package org.example.entity.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkerDaoService {
    private final Connection connection;

    private final PreparedStatement createSt;
    private final PreparedStatement getByIdSt;
    private final PreparedStatement selectMaxIdSt;
    private final PreparedStatement updateSt;
    private final PreparedStatement getAllSt;
    private final PreparedStatement deleteByIdSt;

    public WorkerDaoService(Connection connection) throws SQLException {
        this.connection = connection;
        createSt = connection.prepareStatement(
                "INSERT INTO worker (name, birthday, level, salary) VALUES(?, ?, ?, ?)"
        );

        getByIdSt = connection.prepareStatement(
                "SELECT name, birthday, level, salary FROM worker WHERE id = ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM worker"
        );

        updateSt = connection.prepareStatement(
                "UPDATE worker SET name = ?, birthday = ?, level = ?, salary = ? WHERE id = ?"
        );

        getAllSt = connection.prepareStatement(
                "SELECT id, name, birthday, level, salary FROM worker"
        );

        deleteByIdSt = connection.prepareStatement(
                "DELETE FROM worker WHERE id = ?"
        );
    }


    public long create(Worker worker) throws SQLException {
        createSt.setString(1, worker.getName());
        createSt.setString(2,
                worker.getBirthday() == null ? null : worker.getBirthday().toString());
        createSt.setString(3,
                worker.getLevel() == null ? null : worker.getLevel().name());
        createSt.setLong(4, worker.getSalary());
        createSt.executeUpdate();  // Этот вызов сохраняет запись в БД

        long id;
        try (ResultSet resultSet = selectMaxIdSt.executeQuery()) {
            resultSet.next();
            id = resultSet.getLong("maxId");
        }

        return id;
    }



    public Worker getById(long id) throws SQLException {
        getByIdSt.setLong(1, id);

        try (ResultSet resultSet = getByIdSt.executeQuery()) {
            if (!resultSet.next()) {  // Исправлено
                return null;
            }

            Worker result = new Worker();
            result.setId(id);
            result.setName(resultSet.getString("name"));

            String birthday = resultSet.getString("birthday");
            if (birthday != null) {
                result.setBirthday(LocalDate.parse(birthday));
            }

            String level = resultSet.getString("level");
            if (level != null) {
                result.setLevel(Worker.Level.valueOf(level));
            }
            result.setSalary(resultSet.getLong("salary"));

            return result;
        }
    }


    public void update(Worker worker) throws SQLException {
        updateSt.setString(1, worker.getName());

        updateSt.setString(2,
                worker.getBirthday() == null ? null : worker.getBirthday().toString());
        updateSt.setString(3,
                worker.getLevel() == null ? null : worker.getLevel().name());
        updateSt.setLong(4, worker.getSalary());
        updateSt.setLong(5, worker.getId());

        updateSt.executeUpdate();
    }


    public List<Worker> getAll() throws SQLException {
        try(ResultSet resultSet = getAllSt.executeQuery()) {
            List<Worker> result = new ArrayList<>();

            while (resultSet.next()) {
                Worker worker = new Worker();
                worker.setId(resultSet.getLong("id"));
                worker.setName(resultSet.getString("name"));

                String birthday = resultSet.getString("birthday");
                if (birthday != null) {
                    worker.setBirthday(LocalDate.parse(birthday));
                }

                String level = resultSet.getString("level");
                if (level != null) {
                    worker.setLevel(Worker.Level.valueOf(level));
                }

                worker.setSalary(resultSet.getLong("salary"));
                result.add(worker);
            }
            return result;
        }
    }


    public void deleteById(long id) throws SQLException {
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }


    public void deleteAllWorker() throws SQLException {
        connection.prepareStatement("DELETE FROM project_worker WHERE worker_id IN (SELECT id FROM worker)").executeUpdate();
        connection.prepareStatement("DELETE FROM worker").executeUpdate();

    }
}

