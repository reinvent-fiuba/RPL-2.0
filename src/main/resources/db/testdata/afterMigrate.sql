INSERT INTO users
VALUES (1, 'Alejandro', 'Levinas', 95719, 'alepox', 'levinasale@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        false, 'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (2, 'Matias', 'Cano', 97925, 'tutecano22', 'matiasjosecc@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        false, 'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (3, 'Student', 'Accepted', 00001, 'student_accepted', 'accepted_student@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        false, 'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (4, 'Student', 'NotAccepted', 00001, 'student_not_accepted',
        'not_accepted_student@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        false, 'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (5, 'Student5', 'student5', 00001, 'student5', 'student5@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        false, 'Ing. en Informatica',
        'UBA', now(), now());

INSERT INTO users
VALUES (6, 'Admin', 'admin', 00001, 'superadmin', 'admin@gmail.com',
        '$2a$10$ab9rVz3lVB.ANA2ss.1pOOFwg.tH5yexgXc58PSMwa6CVlBDWM2Eq', true,
        true, '',
        '', now(), now());

INSERT INTO roles
VALUES (1, 'admin',
        'course_delete,course_view,course_edit,activity_view,activity_manage,activity_submit,user_view,user_manage',
        now(), now());

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
        false, '2019-2C', 'http://cholilaonline.com/wp-content/uploads/2018/01/algoritmo-amor.jpg',
        now(), now());

INSERT INTO course_users
VALUES (1, 1, 1, 1, true, now(), now());

INSERT INTO course_users
VALUES (2, 1, 2, 1, true, now(), now());

INSERT INTO course_users
VALUES (3, 1, 3, 2, true, now(), now());

INSERT INTO course_users
VALUES (4, 1, 4, 2, false, now(), now());

INSERT INTO rpl_files
VALUES (1, 'activity_1_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());

INSERT INTO rpl_files
VALUES (2, 'la_submission.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e00dff02425d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047f5376fc69796d28c9457d198d5648af033790a5833ac19ada99073433140a6a01e207dc04e9eabcb0a474baf415c5c53be53bade0993ad241f10a724d271334fc169b672da5f2c138d8336ce8ddde78457354a5df1856edf385ebca08422a024dadd9ad4ba39dc07c19f8927d3f06787ed0ac3ee439d64841b9ff9d9219207814ad0bf9809f4c5b6548aba320556516eb487cf7578b1f4e53cc932868045b557df05b48aaea63ac44d99d57f8192fc11e9f2d725cd6b76ae10bfa372e3282b098d1bec5d926ba652be537bfc6eeba2e9463afda9a44568227da97805715699c2f6360cdaa0eca1bec362d089ee2e84e5cf9feda02bb4c1c6bb9b80151218a417c6033d8408469f98d30532d9946b5b8df3821d6c3599e41cd59cff8e50f39aab1b6935b175ae881fa131ed786ebeca8d088e4b583d15c82fcafdd2eb7dd88bd7fe96760412e26c4d02d1ab98c6c4378707783214d60423af4473ab504b1efc5523702994baa5b0f2d2e5f58d327bdf40207187926948c5453a8380940575f5f24065f80c02d1d4ac00bc339a475f48f8db27d4f85c122da5816a5160bcdf691048fb73527e2128ce7bef57a9959fd8df4e0aed83b0d3696be27f1269d3774157f7e27dd8510931976e0bfd3d6e690912457a15ba32d0ebe344119d9e8f0922159f7f31ab40f2f4ee89159b5db142fd5a598e107b59292143fe5eaced22fc5f7059db7405a98175881ed2a096b1be375b74b8be8e214309f6f80235db73710000008637b8537d12f3910001de04801c00006de384e6b1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (3, 'la_submission_unit_test_c.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e00bff01a85d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047fdc217302bfc7ff2e54c92810a8751c5e0856d69383c7df29e2ac6cf3d78fa63a4246a9df1aba0553fd352aded30665b8ed16109c55f45f3a0734747519162eda31c29b388d92ff35b86f0063a5209930ef3c3434bfb80f426d2468fea4f01e30538ce7af06cdc3bf77fb605506daa068d2fb8c19ff923934cf72cc140260ee23ad3a84e6005c4c3b1801de64f3d7f983d4eee01a59cdd7c5e1da317702c3ea4edbdfd0a93487e1df1d25fc77a8c4b10752f03b95011e0eba543d4f9e8a45fd6995e11578e35dac16e5f261a8bc9b6484525b7b4752aba62f28514f610cb615d27012e23a1305c0de7d0d59e9d21dcf64a6a13e9734e572d7407800fca028472732baeed434fe4f7921844c4093339aa48b6c015ee7fe9a8eb933f70fcaf318fdbbd9828af6c6c25ab27d5dc1b9b2a9ab00813ddb15b7aaf352e97540fb0b8768475493b32cbf1f7724c38c5ea7e4dafff1b2edab54c31ec1a6385c5e44570aa20043bdfa88d82f4e97733adbafe0646e73af1e61b68e528294efdd9cde4db02f538cd0000dee298736358de780001c403801800005cb6e0f1b1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (4, 'c_unit_test.c', 'text/x-c',
        x'23696e636c756465203c637269746572696f6e2f637269746572696f6e2e683e0a23696e636c75646520226d61696e2e63220a0a54657374286d6973632c20746573744e616d653129207b0a2020202063725f61737365727428666f6f4e6f526570657469646f2829203d3d2031293b0a7d0a0a54657374286d6973632c20746573744e616d653229207b0a2020202063725f617373657274286261724e6f526570657469646f2829203d3d2032293b0a7d',
        now(), now());

INSERT INTO rpl_files
VALUES (5, 'unit_test.py', 'text/x-c',
        x'696d706f727420756e6974746573740a696d706f72742061737369676e6d656e745f6d61696e0a0a636c61737320546573744d6574686f647328756e6974746573742e5465737443617365293a0a0a202064656620746573745f312873656c66293a0a2020202073656c662e617373657274547275652861737369676e6d656e745f6d61696e2e666f6f4e6f526570657469646f2829290a0a202064656620746573745f322873656c66293a0a2020202073656c662e617373657274547275652861737369676e6d656e745f6d61696e2e6261724e6f526570657469646f2829290a',
        now(), now());

INSERT INTO rpl_files
VALUES (6, 'la_submission_unit_test_python.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e007ff013d5d00309cecd2685724bcae385acc81d022c228fc8542d451fd62279b02c1354277cb3b379047fdcb18ebffc2baf4895bf9ce8e52c11db7231052f6f024de9881b6620db9bed2fce29878d5c0d759c9c6e4853b4e712bb9f2c42b243c174b7d5df56d4328fc552eebdaa8dc59c137e76ce01191fcb3361cfd805e9ed5a9c19f4071d256c910152a1274591005748a827ce930a7e0488bb9638539373cb107b33b739f3f6dd042fa954ffd2176947f6af52d1b7c8586fa90bcad59e93f29fd32f5d604de44bbea65d4515f4903a8edf8d4e4c7b93413dd697ec6ea02cffaa4df5e483e00be69479ca5079f1c57008ef24c168199f2dab32357698632030f07f781525dde6b9f124d9628a0748f137cdefdd459e173b372b418a381c95405db2613a8133bf6fa489fbb64d2e57ec67e269f506b773bd185780046d37bff2c2f000000000049b256dd9240d1a60001d9028010000011dfdb7db1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (7, 'multi_submission_c_unit_test.tar.xz', 'text/x-c',
        x'fd377a585a000004e6d6b4460200210116000000742fe5a3e011ff01ce5d0036984972850270e93a4742c2b005c0f9915c9e4405b5de90fd4154b1a141aa4f3e98954290b8b3da7d29a45adda34ebac0f541e09a1b6987d8026136bb331c91f18794cf969b3a641380074914bd2bc492bf571c6a171d59703945ca0a005e7bb9a4d9876b22b092deb1ee65ded485ae0ab7291bd333495f58c37e3e28f78832c8ae017a24de721524a06bf737b77e65d9b4164020158fd78fd643c46ace8465b4fd390fa08907fe507dfc6b3998939fcf087c8cded1770e7d6d4c56c897f0a12910a9a2732312f1c6c7034001372dc40b6b9c1b2c1db101f8c42241620b0af437dc48a8ca064ecce2885395d7a601df277aa23068c13fcac508ef61aa2152e851e71ff2d86eadebc90077a2bdee8280a435302e17135e44e944acc91aeb012f7ce121b926f5668da04353ac020346ce9234616c675185d12b34192b9f479195388d210f655d7fc0b5d573e8f53d17f20f580c9a6ac73f174d31e2fcffef02419d4b1132284253f5e020d00074299ef0f785b8bc08d2b51c1341c23545e2518ca8ce172db1d8ad54f7f3f3cf93a9583ec250a1cbd436240a6609760811d975c214a4a8d094d0bf890e13ded67ab4b27960222cf67fd86bbe18111623b1b07188607126d7c85fa47baafb47ae762a000000a655d15ce5c5f5030001ea0380240000eece37e1b1c467fb020000000004595a',
        now(), now());

INSERT INTO rpl_files
VALUES (8, 'activity_2_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());
INSERT INTO rpl_files
VALUES (9, 'activity_3_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800069dea5e0003edd4eb6a83301407703ffb1407cbc04269a3550b0ef7240309d3b6428d62226c8cbefb9254bbee46b7b12bfc7f947ac9c9495a3d874b596d445d0a95d7bc12f3f6cef9724c4b92c81c8355cc4e8f561c31275826ab300ea238081d1644411c3bc4be7e2b2ff552f18ec8e1bbb26d6edf8e3b37fe4f5575db748a5455ea13d7758b724de645f0a7a94bc3ed5c969b5e148da48c2aa1fc4ab4bdf2a75357476ce58cea4acc48c7e8e1c384f93821e7f9b6e9b8cc7548aff4f538e03fcb6c734da8ed747ef2bd0b1616e9f0b59a47eb6be1cdf44a66213d637a69a26dacaf4f88bc7bbb4acaaec27d7a3fac355c8d2ba436d1de9baf9baee6cab733b261fb263e33d96dbacf1897c9ccfef4af710f7fa2fbdbcff79ce1817d47d91f9dabffe52a1eeb3fd61f5dffe13260a8ff9f60eafde3d56a7ac3e4a629aa4d4345b923beeb6bd198a2e452da4adeca6337786c1f8b052d13c66cc0cbd6f2fccec563b0ded0ebe992c3f869f3394d7018b6215da9fa4e3ce9577fbe3801000000000000000000000000000000dee101fd71fd3600280000',
        now(), now());
INSERT INTO rpl_files
VALUES (10, 'activity_4_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800069dea5e0003edd4eb6a83301407703ffb1407cbc04269a3550b0ef7240309d3b6428d62226c8cbefb9254bbee46b7b12bfc7f947ac9c9495a3d874b596d445d0a95d7bc12f3f6cef9724c4b92c81c8355cc4e8f561c31275826ab300ea238081d1644411c3bc4be7e2b2ff552f18ec8e1bbb26d6edf8e3b37fe4f5575db748a5455ea13d7758b724de645f0a7a94bc3ed5c969b5e148da48c2aa1fc4ab4bdf2a75357476ce58cea4acc48c7e8e1c384f93821e7f9b6e9b8cc7548aff4f538e03fcb6c734da8ed747ef2bd0b1616e9f0b59a47eb6be1cdf44a66213d637a69a26dacaf4f88bc7bbb4acaaec27d7a3fac355c8d2ba436d1de9baf9baee6cab733b261fb263e33d96dbacf1897c9ccfef4af710f7fa2fbdbcff79ce1817d47d91f9dabffe52a1eeb3fd61f5dffe13260a8ff9f60eafde3d56a7ac3e4a629aa4d4345b923beeb6bd198a2e452da4adeca6337786c1f8b052d13c66cc0cbd6f2fccec563b0ded0ebe992c3f869f3394d7018b6215da9fa4e3ce9577fbe3801000000000000000000000000000000dee101fd71fd3600280000',
        now(), now());
INSERT INTO rpl_files
VALUES (11, 'activity_5_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());
INSERT INTO rpl_files
VALUES (12, 'activity_6_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());
INSERT INTO rpl_files
VALUES (13, 'activity_7_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());
INSERT INTO rpl_files
VALUES (14, 'activity_8_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());

INSERT INTO rpl_files
VALUES (15, 'activity_8_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());

INSERT INTO rpl_files
VALUES (16, 'activity_8_starting_filess.tar.gz', 'text/x-c',
        x'1f8b0800d89dea5e0003edd34f6b8330180670cff914c1b1d21691c4f907e6d8a5f4b07337d8a12059b555b0b11885c1d8775f4c758cd2d1cbbab2f1fc0eb14dde3751f4d98a42ba2bebac9816867e77e551c0be5e0d3f882c7e13465ec0fd807b16e33cf0428bb2f3ded65eab1a51536a8932db55afdfd79d5affa3ae0ab92adb34a377aa49cbe2c5cdefc9e79cdd14d97657b9b94d08296443b7fa6b194fe81ba15a37b12f480ab9a93325d22a266649ad845cd3b17d9ddacee8b066d217990d0be9d05cc566625d56a2a12adb0cbb649b56a6954a449257b55089ae6e1bfd7f58181f6eedd051aef460b61de9b2e1ac5dad4f3377c4bcf4b61f22d75f2fa5ede896ae615fde5577439d356d2de9fcf9e131593ccd66f3c52226ef977e5d3f6e78c3e73ce354fe79e41fe4dfe38c21ffbfe158fe4dd44f87ef58fe1d93eaa9e9e87ff77d4e1fefe9d03f892ffdec00000000000000000000000000000000ffc107f20e928f00280000',
        now(), now());

INSERT INTO activity_categories
VALUES (1, 1, 'Conceptos Basicos',
        'Ejercicios faciles para empezar', true, now(), now());


INSERT INTO activities
VALUES (1, 1, 1, 'Pasar de segundos a horas, minutos, segundos IO Test',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true,
        true, false, '//El codigo inicial', 1, 10, now(), now());

INSERT INTO activities
VALUES (2, 1, 1, 'Ej2 Unit Test C',
        'La descripción del ejericio 2', 'c_std11', false, true, false, '//El codigo inicial', 8,
        10,
        now(), now());

INSERT INTO activities
VALUES (3, 1, 1, 'Ej 3 Unit Test Python',
        'La descripción del ejericio 3', 'python_3.7', false, true, false, '#El codigo inicial', 9,
        10,
        now(), now());

INSERT INTO activities
VALUES (4, 1, 1, 'Ej 4 IO Test Python',
        'La descripción del ejericio 4', 'python_3.7', true, true, false, '#El codigo inicial', 10,
        10,
        now(), now());

INSERT INTO activities
VALUES (5, 1, 1, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, true, false, '//El codigo inicial', 11,
        10,
        now(), now());

INSERT INTO activity_categories
VALUES (2, 1, 'Conceptos no tan Basicos',
        'Ejercicios un poquito más dificiles', true, now(), now());


INSERT INTO activities
VALUES (6, 1, 2, 'Pasar de segundos a horas, minutos, segundos',
        'Dada una cantidad de segundos, devolver/imprimir la cantidad de horas', 'c_std11', true,
        true, false, '//El codigo inicial', 12, 10,
        now(), now());

INSERT INTO activities
VALUES (7, 1, 2, 'El ejericio 2',
        'La descripción del ejericio 2', 'c_std11', true, true, false, '//El codigo inicial', 13,
        10,
        now(), now());

INSERT INTO activities
VALUES (8, 1, 2, 'El ejericio 3',
        'La descripción del ejericio 3', 'c_std11', true, true, false, '//El codigo inicial', 14,
        10,
        now(), now());

INSERT INTO activities
VALUES (9, 1, 2, 'El ejericio 4',
        'La descripción del ejericio 4', 'c_std11', true, true, false, '//El codigo inicial', 15,
        10,
        now(), now());

INSERT INTO activities
VALUES (10, 1, 2, 'El ejericio 5',
        'La descripción del ejericio 5', 'c_std11', true, true, false, '//El codigo inicial', 16,
        10,
        now(), now());

INSERT INTO activity_submissions
VALUES (1, 1, 1, 2, 'PENDING', now(), now());

INSERT INTO activity_submissions
VALUES (2, 2, 1, 3, 'PENDING', now(), now());

INSERT INTO activity_submissions
VALUES (3, 3, 1, 6, 'PENDING', now(), now());

INSERT INTO activity_submissions
VALUES (4, 1, 1, 7, 'PENDING', now(), now());

-- INSERT INTO tests
-- VALUES (1, 1, now(), now());

INSERT INTO results
VALUES (1, 1, 'passed', now(), now());

INSERT INTO test_run
VALUES (1, 1, true, 'salio todo bien', 'todo el stderr', 'todo el stdout', now(), now());

INSERT INTO unit_tests
VALUES (1, 2, 4, now(), now());

INSERT INTO unit_tests
VALUES (2, 3, 5, now(), now());

INSERT INTO IO_tests
VALUES (1, 1, '26164', '07:16:04.0000', now(), now());
INSERT INTO IO_tests
VALUES (2, 1, '26165', '07:16:05.0000', now(), now());

INSERT INTO IO_tests
VALUES (3, 4, '26164', '07:16:04.0000', now(), now());
INSERT INTO IO_tests
VALUES (4, 4, '26165', '07:16:05.0000', now(), now());