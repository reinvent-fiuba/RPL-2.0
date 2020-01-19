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
VALUES (1, 'admin', 'course_create,course_edit,activity_submit', now(), now());

INSERT INTO roles
VALUES (2, 'student', 'activity_submit', now(), now());

INSERT INTO permissions
VALUES (1, 'course_create', now());

INSERT INTO permissions
VALUES (2, 'course_edit', now());

INSERT INTO permissions
VALUES (3, 'activity_submit', now());

INSERT INTO courses
VALUES (1, 'Algoritmos y Programación I', 'UBA', 'Hola a todos! Bienvenidos al curso....', true,
        '2019-2C', 'http://cholilaonline.com/wp-content/uploads/2018/01/algoritmo-amor.jpg', now(),
        now());

INSERT INTO course_users
VALUES (1, 1, 1, 1, true, now(), now());

INSERT INTO course_users
VALUES (2, 1, 2, 1, true, now(), now());

INSERT INTO rpl_files
VALUES (1, 'la_submission.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e015ff02665d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047f5376fc69796d28c9457d198d5648af033790a5833ac19ada99073433140a6a01e207dc04e9eabcb0a474baf415c5c53be53bade0993ad241f10a724d271334fc169b672da5f2c138d8336ce8ddde78457354a5df1856edf385ebca08422a024dadd9ad4ba39dc07c19f8927d3f06787ed0ac3ee439d64841b9ff9d9219207814ad0bf9809f4c5b6548aba320556516eb487cf7578b1f4e53cc932868045b557df05b48aaea63ac44d99d57f8192fc11e9f2d725cd6b76ae10bfa372e3282b098d1bec5d926ba652be537bfc6eeba2e9463afda9a44568227da97805715695e0fc09ed30231fd9cf026e5c8013fdc2dec7b050c9e55f0c9f40f5f24809e014433e36f3a42ac0c9155acabf96e4d0da0af50e84289ac318bd84cc1ef30119292ae614e33e98b007a79d21c62473dc6c06dc060d93bdbc04f5fc816588f5525ee2694dd274f7dc78b67dc0d4e55ac0a1d80ba0716117daf23e1372bafc771c53391c57c46ab2383c7ef62d7852a671a3e9212799a96909d4a847e9aba6eb54ae41711ddf33d79df6b36f796389b8dd98daacab93e6abc951b9f34d92291bc67150766c4832d149482c9d7a73d3eaedd7595700cbb066ba8df206e659c7dd30584ef9e00311802c271b7179fef50b5f8fb4ad4388bce192caea0f45ed38e06c8021840419525d9e21ef26be6bebaa6cf489cfafed3935ad5b9c91f47b345ce8715b1383d5d5d48e6ef971bce1c872553ba1677fb83ee9fa9a575426f4f8b5b876ec6d76fe9befc3fd05f3a59dcbbf13e06eead7991cbfb62b132905aa11e7bdfbb1fe00000000945b4770a62890d900018205802c0000c1e7c384b1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (2, 'c_unit_test.c', 'text/x-c',
        x'23696e636c756465202267746573742f67746573742e68220a23696e636c75646520226578616d706c652e68220a0a54455354286578616d706c652c20616464290a7b0a20202020646f75626c65207265733b0a20202020726573203d206164645f6e756d6265727328312e302c20322e30293b0a202020204153534552545f4e454152287265732c20332e302c20312e30652d3131293b0a7d',
        now(), now());

INSERT INTO activity_categories
<<<<<<< HEAD
VALUES (1, 1, 'Conceptos Basicos',
        'Ejercicios faciles para empezar', true, now(), now());


INSERT INTO activities
VALUES (1, 1, 1, 'Pasar de segundos a horas, minutos, segundos',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (2, 1, 1, 'El ejericio 2',
        'La descripción del ejericio 2', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (3, 1, 1, 'El ejericio 3',
        'La descripción del ejericio 3', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (4, 1, 1, 'El ejericio 4',
        'La descripción del ejericio 4', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (5, 1, 1, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, 1,
        now(), now());

INSERT INTO activity_categories
VALUES (2, 1, 'Conceptos no tan Basicos',
        'Ejercicios un poquito más dificiles', true, now(), now());


INSERT INTO activities
VALUES (6, 1, 2, 'Pasar de segundos a horas, minutos, segundos',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (7, 1, 2, 'El ejericio 2',
        'La descripción del ejericio 2', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (8, 1, 2, 'El ejericio 3',
        'La descripción del ejericio 3', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (9, 1, 2, 'El ejericio 4',
        'La descripción del ejericio 4', 'c_std11', true, 1,
        now(), now());

INSERT INTO activities
VALUES (10, 1, 2, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, 1,
        now(), now());

INSERT INTO activity_submissions
VALUES (1, 1, 1, 1, 'PENDING', now(), now());

-- INSERT INTO tests
-- VALUES (1, 1, now(), now());

INSERT INTO results
VALUES (1, 1, 'passed', now(), now());

INSERT INTO test_run
VALUES (1, 1, true, 'salio todo bien', 'todo el stderr', 'todo el stdout', now(), now());

INSERT INTO unit_tests
VALUES (1, 1, 2, now(), now());

INSERT INTO IO_tests
VALUES (1, 1, '26164', '07:16:04.0000', now(), now());
INSERT INTO IO_tests
VALUES (2, 1, '26165', '07:16:05.0000', now(), now());