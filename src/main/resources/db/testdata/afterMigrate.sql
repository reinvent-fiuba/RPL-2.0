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

INSERT INTO users
VALUES (3, 'Student', 'Accepted', 00001, 'student_accepted', 'accepted_student@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', false,
        'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (4, 'Student', 'NotAccepted', 00001, 'student_not_accepted', 'not_accepted_student@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', false,
        'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO roles
VALUES (1, 'admin', 'course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage', now(), now());

INSERT INTO roles
VALUES (2, 'student', 'course_view,activity_view,activity_submit,user_view', now(), now());

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

INSERT INTO course_users
VALUES (3, 1, 3, 2, true, now(), now());

INSERT INTO course_users
VALUES (4, 1, 4, 2, false, now(), now());

INSERT INTO rpl_files
VALUES (1, 'activity_1_supporting_files.tar.gz', 'text/x-c',
x'1f8b0800af122e5e0003edd2bf0ac23010c7f13ccaad2e92d87fe0ee83847a60a5b63569c4c737bae8224e45c4ef67f941ee86cb7141fdfea4ebf93a9bc5d8acaecb7bbaa6b2aff9e09c35aea85d51daa6292a63dda6dc382376b9919e529c7d1031bed769bcbeeffb54ff51bb38ab689434880feda1bb8c926f41ce49a51fa3e49736c79474af83a4985735f9e025a6287ad4d0766d97ebdbd5b7ff0100000000000000000000000000ffe606912b5d9c00280000',
now(), now());

INSERT INTO rpl_files
VALUES (2, 'la_submission.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e00dff02425d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047f5376fc69796d28c9457d198d5648af033790a5833ac19ada99073433140a6a01e207dc04e9eabcb0a474baf415c5c53be53bade0993ad241f10a724d271334fc169b672da5f2c138d8336ce8ddde78457354a5df1856edf385ebca08422a024dadd9ad4ba39dc07c19f8927d3f06787ed0ac3ee439d64841b9ff9d9219207814ad0bf9809f4c5b6548aba320556516eb487cf7578b1f4e53cc932868045b557df05b48aaea63ac44d99d57f8192fc11e9f2d725cd6b76ae10bfa372e3282b098d1bec5d926ba652be537bfc6eeba2e9463afda9a44568227da97805715699c2f6360cdaa0eca1bec362d089ee2e84e5cf9feda02bb4c1c6bb9b80151218a417c6033d8408469f98d30532d9946b5b8df3821d6c3599e41cd59cff8e50f39aab1b6935b175ae881fa131ed786ebeca8d088e4b583d15c82fcafdd2eb7dd88bd7fe96760412e26c4d02d1ab98c6c4378707783214d60423af4473ab504b1efc5523702994baa5b0f2d2e5f58d327bdf40207187926948c5453a8380940575f5f24065f80c02d1d4ac00bc339a475f48f8db27d4f85c122da5816a5160bcdf691048fb73527e2128ce7bef57a9959fd8df4e0aed83b0d3696be27f1269d3774157f7e27dd8510931976e0bfd3d6e690912457a15ba32d0ebe344119d9e8f0922159f7f31ab40f2f4ee89159b5db142fd5a598e107b59292143fe5eaced22fc5f7059db7405a98175881ed2a096b1be375b74b8be8e214309f6f80235db73710000008637b8537d12f3910001de04801c00006de384e6b1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (3, 'la_submission_unit_test.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e00bff01915d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047f96fd7171227ab74bedf9da4e615e7366ec3dafd3eb5f492f4ae88fdf186739c9936dd18ff2f4e91c89ca4771a9d7d32061aa6b15e6a9829dd4c17ef8e65482a92f8ff828853d8748f7e494fdd6454c67fe8bb0cc94bff577bcdc573f9052b2a977e6a4f01ab353a4201f762f8ea244c060e4fd3cac3e13086716942b07f7b3f884d28bfddb2360d72012b8aa7e7cfd3c5a2bb94d7843b57a92c32a91b3cae66f1e25b56e7318a3bdc841594eab48c2910e8e1c1682dd198497f36fa2de156ffceab4d4536b9ec87dcb5651e52accb6509f7768d1350453a7aedeadf048cb6f072ae2eafcffd8d986ee128a425373f4105d89ee795f03891d642af858ad17e94df80e18b1b0ccb3655a4f191da3e129d70d172adc6c24b758d7b753a72bcefdd563f994d41edbacb7a83c0bef4fd7b1c60c7fa2637715a853580465c6005809b91e522742f44a8516329096ce757017dea111069417fd502b1fa22714d9d8152793a0b130f04e45138d012e80000000000413a7a0dbf2d70fa0001ad0380180000cee81bdeb1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (4, 'c_unit_test.c', 'text/x-c',
        x'23696e636c756465203c637269746572696f6e2f637269746572696f6e2e683e0a23696e636c75646520226d61696e2e63220a0a54657374286d6973632c206661696c696e6729207b0a2020202063725f61737365727428666f6f4e6f526570657469646f2829203d3d2031293b0a7d0a0a54657374286d6973632c2070617373696e6729207b0a2020202063725f617373657274286261724e6f526570657469646f2829203d3d2032293b0a7d',
        now(), now());

INSERT INTO activity_categories
VALUES (1, 1, 'Conceptos Basicos',
        'Ejercicios faciles para empezar', true, now(), now());


INSERT INTO activities
VALUES (1, 1, 1, 'Pasar de segundos a horas, minutos, segundos',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true,
        '//El codigo inicial', 1, now(), now());

INSERT INTO activities
VALUES (2, 1, 1, 'El ejericio 2',
        'La descripción del ejericio 2', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (3, 1, 1, 'El ejericio 3',
        'La descripción del ejericio 3', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (4, 1, 1, 'El ejericio 4',
        'La descripción del ejericio 4', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (5, 1, 1, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activity_categories
VALUES (2, 1, 'Conceptos no tan Basicos',
        'Ejercicios un poquito más dificiles', true, now(), now());


INSERT INTO activities
VALUES (6, 1, 2, 'Pasar de segundos a horas, minutos, segundos',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true,
        '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (7, 1, 2, 'El ejericio 2',
        'La descripción del ejericio 2', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (8, 1, 2, 'El ejericio 3',
        'La descripción del ejericio 3', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (9, 1, 2, 'El ejericio 4',
        'La descripción del ejericio 4', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activities
VALUES (10, 1, 2, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, '//El codigo inicial', 1,
        now(), now());

INSERT INTO activity_submissions
VALUES (1, 1, 1, 2, 'PENDING', now(), now());

INSERT INTO activity_submissions
VALUES (2, 2, 1, 3, 'PENDING', now(), now());

-- INSERT INTO tests
-- VALUES (1, 1, now(), now());

INSERT INTO results
VALUES (1, 1, 'passed', now(), now());

INSERT INTO test_run
VALUES (1, 1, true, 'salio todo bien', 'todo el stderr', 'todo el stdout', now(), now());

INSERT INTO unit_tests
VALUES (1, 2, 4, now(), now());

INSERT INTO IO_tests
VALUES (1, 1, '26164', '07:16:04.0000', now(), now());
INSERT INTO IO_tests
VALUES (2, 1, '26165', '07:16:05.0000', now(), now());