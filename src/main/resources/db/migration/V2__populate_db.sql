-- Додавання працівників
INSERT INTO worker (name, birthday, level, salary) VALUES
    ('Alice', '1985-05-15', 'Senior', 6000),
    ('Bob', '1990-07-22', 'Middle', 4500),
    ('Charlie', '1995-08-10', 'Junior', 2500),
    ('David', '2000-01-02', 'Trainee', 800),
    ('Eva', '1992-09-12', 'Senior', 7000),
    ('Frank', '1988-03-05', 'Middle', 3200),
    ('Grace', '1994-06-25', 'Junior', 2300),
    ('Hannah', '1997-11-30', 'Trainee', 950),
    ('Ian', '1983-12-14', 'Senior', 5500),
    ('Jack', '1989-04-18', 'Middle', 3800),
    ('Yevhenii', '2002-10-23', 'Junior', 2000);

-- Додавання клієнтів
INSERT INTO client (name) VALUES
    ('Acme Corp'),
    ('Tech Innovators'),
    ('Business Solutions'),
    ('Creative Minds'),
    ('Enterprise Partners');

-- Додавання проєктів
INSERT INTO project (name, client_id, start_date, finish_date) VALUES
    ('Project Alpha', 1, '2023-01-01', '2023-06-01'),
    ('Project Beta', 2, '2023-02-01', '2023-09-01'),
    ('Project Gamma', 3, '2023-03-01', '2024-01-01'),
    ('Project Delta', 4, '2023-04-01', '2023-10-01'),
    ('Project Epsilon', 5, '2023-05-01', '2023-12-01'),
    ('Project Zeta', 1, '2023-06-01', '2024-02-01'),
    ('Project Eta', 2, '2023-07-01', '2023-11-01'),
    ('Project Theta', 3, '2023-08-01', '2024-03-01'),
    ('Project Iota', 4, '2023-09-01', '2023-11-01'),
    ('Project Kappa', 5, '2023-10-01', '2024-04-01');

-- Призначення працівників на проєкти
INSERT INTO project_worker (project_id, worker_id) VALUES
    (1, 1), (1, 2), (2, 3), (2, 4), (3, 5),
    (3, 6), (4, 7), (4, 8), (5, 9), (5, 10),
    (6, 1), (6, 3), (7, 2), (7, 4), (8, 5),
    (8, 6), (9, 7), (10, 8), (10, 9), (10, 10);
