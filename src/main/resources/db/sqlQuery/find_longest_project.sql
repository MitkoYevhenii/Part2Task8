SELECT name, DATEDIFF(month, start_date, finish_date) AS month_count
FROM project
WHERE DATEDIFF(month, start_date, finish_date) = (
    SELECT MAX(DATEDIFF(month, start_date, finish_date))
    FROM project
    );

