ALTER TABLE project_worker
ADD CONSTRAINT fk_worker
FOREIGN KEY (worker_id)
REFERENCES worker(id)
ON DELETE CASCADE;
