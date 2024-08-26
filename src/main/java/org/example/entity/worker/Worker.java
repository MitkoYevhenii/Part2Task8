package org.example.entity.worker;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Worker {
    private long id;
    private String name;
    private LocalDate birthday;
    private Level level;
    private long salary;

    public enum Level {
        Trainee,
        Junior,
        Middle,
        Senior,
    }
}
