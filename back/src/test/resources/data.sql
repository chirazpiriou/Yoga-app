
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
('User', 'User', false, 'user@studio.com', '$2a$10$Q5SxEqJhXYc3T.j0b0x71OGymzg5OByx6HvmqWs62b2MAT5m5OYxy'); 

INSERT INTO SESSIONS (name, description, teacher_id, date)
VALUES
  ('Session1', 'Description1', 1, '2025-01-05 00:00:00'),
  ('Session2', 'Description2', 1, '2025-01-06 00:00:00');


