INSERT INTO users
VALUES (1, 'Alejandro', 'Levinas', 95719, 'alepox', 'levinasale@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', false,
        'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (2, 'Matias', 'Cano', 97925, 'tute', 'matiasjosecc@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', false,
        'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO roles
VALUES (1, 'admin', 'course_create,course_edit', now(), now());

INSERT INTO permissions
VALUES (1, 'course_create', now());

INSERT INTO permissions
VALUES (2, 'course_edit', now());

INSERT INTO courses
VALUES (1, 'Algoritmos y Programación I', 'UBA', now(), now());

INSERT INTO courses_semester
VALUES (1, 1, 'Hola a todos! Bienvenidos al curso....', true, '2019-2C', now(), now());

INSERT INTO course_users
VALUES (1, 1, 1, 1, true, now(), now());

INSERT INTO course_users
VALUES (2, 1, 2, 1, true, now(), now());

INSERT INTO files
VALUES (1, 1, now(), now());

INSERT INTO activities
VALUES (1, 1, 'Imprimir Hola Mundo!!', 'Imprime Hola mundo y ganaras', 'c_std11', true, 1, now(),
        now());

INSERT INTO activity_submissions
VALUES (1, 1, 1, 1, 'running', now(), now());

INSERT INTO tests
VALUES (1, 1, now(), now());

INSERT INTO results
VALUES (1, 1, 1, 'passed', now(), now());

INSERT INTO test_results
VALUES (1, 1, true, 'todo el stderr', 'todo el stdout', now(), now());

INSERT INTO unit_tests
VALUES (1, 1, 1, now(), now());

INSERT INTO IO_tests
VALUES (1, 1, 'Hello World!', 'Hello World!', now(), now());