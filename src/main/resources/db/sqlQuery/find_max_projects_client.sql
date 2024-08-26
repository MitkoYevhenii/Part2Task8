SELECT c.name, COUNT(p.id) AS project_count
FROM client c
         JOIN project p ON c.id = p.client_id
GROUP BY c.name
HAVING COUNT(p.id) = (
    SELECT MAX(project_count)
    FROM (
             SELECT COUNT(p.id) AS project_count
             FROM project p
             GROUP BY p.client_id
         ) AS project_counts
);
