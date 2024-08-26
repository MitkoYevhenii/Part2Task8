package org.example.entity.worker;

import com.github.javafaker.Faker;
import org.example.prefs.Prefs;
import org.example.storage.DatabaseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class WorkerDaoServiceTest {
    Prefs prefs = new Prefs();
    private final String dbUrl = prefs.getString(Prefs.DB_JDBC_TEST_CONNECTION_URL).concat(";DB_CLOSE_DELAY=-1");
    private final String dbUser = prefs.getString(Prefs.DB_JDBC_USERNAME);
    private final String dbPass = prefs.getString(Prefs.DB_JDBC_PASSWORD);

    private WorkerDaoService daoService;
    private Connection connection;
    private Faker faker;
    private Random random;


    @BeforeEach
    public void beforeEach() throws SQLException {
        new DatabaseService().initDatabase(dbUrl, dbUser, dbPass);
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        daoService = new WorkerDaoService(connection);
        faker = new Faker();
        random = new Random();
    }


    @Test
    public void testWorkerCreateCorrectly() throws SQLException {
        List<Worker> randomWorkerList = new ArrayList<>();

        Worker bestJunior = new Worker();
        bestJunior.setName("Yevhenii");  // Должно быть не менее 2 символов
        bestJunior.setBirthday(LocalDate.of(2002, 10, 23));  // Дата должна быть после 1900 года
        bestJunior.setLevel(Worker.Level.Junior);  // Уровень должен быть одним из допустимых значений
        bestJunior.setSalary(5000);  // Зарплата должна быть в пределах от 100 до 100000
        randomWorkerList.add(bestJunior);

        for (int i = 0; i < 100; i++) {  // Генерация 10 случайных работников
            Worker worker = new Worker();
            worker.setName(faker.name().fullName());  // Генерация случайного имени
            worker.setBirthday(faker.date().birthday(18, 65).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());  // Генерация случайной даты рождения
            worker.setLevel(Worker.Level.values()[random.nextInt(Worker.Level.values().length)]);  // Случайный уровень
            worker.setSalary(faker.number().numberBetween(100, 100000));  // Случайная зарплата
            randomWorkerList.add(worker);
        }

        for (Worker worker : randomWorkerList) {
            final long id = daoService.create(worker);
            Worker saved = daoService.getById(id);

            Assertions.assertEquals(id, saved.getId());
            Assertions.assertEquals(worker.getName(), saved.getName());
            Assertions.assertEquals(worker.getBirthday(), saved.getBirthday());
            Assertions.assertEquals(worker.getLevel(), saved.getLevel());
            Assertions.assertEquals(worker.getSalary(), saved.getSalary());
        }
    }


    @Test
    public void testUpdate() throws SQLException {
        //Set up
        Worker worker = new Worker();
        worker.setName("Test name");
        worker.setBirthday(LocalDate.of(1990, 5, 15));
        worker.setLevel(Worker.Level.Middle);
        worker.setSalary(2000);

        long id = daoService.create(worker);
        worker.setId(id);

        //Update
        worker.setName("Updated Name");
        worker.setBirthday(LocalDate.of(1985, 10, 10));
        worker.setLevel(Worker.Level.Senior);
        worker.setSalary(3000);
        daoService.update(worker);


        //Get by id and compile
        Worker updatedWorker = daoService.getById(id);
        Assertions.assertEquals(id, updatedWorker.getId());
        Assertions.assertEquals(worker.getName(), updatedWorker.getName());
        Assertions.assertEquals(worker.getBirthday(), updatedWorker.getBirthday());
        Assertions.assertEquals(worker.getLevel(), updatedWorker.getLevel());
        Assertions.assertEquals(worker.getSalary(), updatedWorker.getSalary());
    }


    @Test
    public void testDelete() throws SQLException {
        Worker expected = new Worker();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.of(2018, 12, 22));
        expected.setLevel(Worker.Level.Trainee);
        expected.setSalary(5000);

        long id = daoService.create(expected);
        daoService.deleteById(id);

        Assertions.assertNull(daoService.getById(id));
    }


    @Test
    public void testDeleteAllWorkers() throws SQLException {
        List<Worker> randomWorkerList = new ArrayList<>();

        Worker bestJunior = new Worker();
        bestJunior.setName("Yevhenii");  // Должно быть не менее 2 символов
        bestJunior.setBirthday(LocalDate.of(2002, 10, 23));  // Дата должна быть после 1900 года
        bestJunior.setLevel(Worker.Level.Junior);  // Уровень должен быть одним из допустимых значений
        bestJunior.setSalary(5000);  // Зарплата должна быть в пределах от 100 до 100000
        randomWorkerList.add(bestJunior);

        for (int i = 0; i < 100; i++) {  // Генерация 100 случайных работников
            Worker worker = new Worker();
            worker.setName(faker.name().fullName());  // Генерация случайного имени
            worker.setBirthday(faker.date().birthday(18, 65).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());  // Генерация случайной даты рождения
            worker.setLevel(Worker.Level.values()[random.nextInt(Worker.Level.values().length)]);  // Случайный уровень
            worker.setSalary(faker.number().numberBetween(100, 100000));  // Случайная зарплата
            randomWorkerList.add(worker);

            final long id = daoService.create(worker);
            Worker saved = daoService.getById(id);
            Assertions.assertNotNull(saved, "Worker should be saved successfully");
        }

        // Удаление всех работников
        daoService.deleteAllWorker();

        // Проверка, что все записи удалены
        List<Worker> allWorkers = daoService.getAll();
        Assertions.assertTrue(allWorkers.isEmpty(), "All workers should be deleted");

        // Проверка, что таблица пуста
        Assertions.assertEquals(0, allWorkers.size(), "Worker table should be empty after deleteAllWorker");
    }


    @Test
    public void testGetAll() throws SQLException {
        daoService.deleteAllWorker();
        Worker expected = new Worker();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.of(2018, 12, 22));
        expected.setLevel(Worker.Level.Trainee);
        expected.setSalary(5000);

        long id = daoService.create(expected);
        expected.setId(id);

        final List<Worker> expectedHuman = Collections.singletonList(expected);
        List<Worker> actualHumans = daoService.getAll();

        Assertions.assertEquals(expectedHuman, actualHumans);
    }


    @AfterEach
    public void afterEach() throws SQLException {
        new DatabaseService().cleanDatabase(dbUrl, dbUser, dbPass);
        connection.close();
    }
}
