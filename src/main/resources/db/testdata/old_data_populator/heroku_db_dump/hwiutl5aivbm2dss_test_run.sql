INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (1, 1, 1, 'salio todo bien', 'todo el stderr', 'todo el stdout', '2020-07-06 04:09:43',
        '2020-07-06 04:09:43');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (2, 6, 0, 'Error en Running Unit Tests. Codigo Error 2', 'make: *** No rule to make target ''run_unit_test''.
', '2020-07-06 04:40:30,146 RPL-2.0      INFO     Build Started
2020-07-06 04:40:30,148 RPL-2.0      INFO     Building
2020-07-06 04:40:30,148 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  unit_test.py tiempo.py unit_test_wrapper.py assignment_main.py
2020-07-06 04:40:30,183 RPL-2.0      INFO     end_BUILD
2020-07-06 04:40:30,183 RPL-2.0      INFO     Build Ended
2020-07-06 04:40:30,183 RPL-2.0      INFO     Run Started
2020-07-06 04:40:30,184 RPL-2.0      INFO     Running Unit Tests
2020-07-06 04:40:30,184 RPL-2.0      INFO     start_RUN

2020-07-06 04:40:30,186 RPL-2.0      INFO     end_RUN
2020-07-06 04:40:30,186 RPL-2.0      INFO     RUN ERROR
', '2020-07-06 04:40:31', '2020-07-06 04:40:31');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (3, 7, 0, 'Error en Running Unit Tests. Codigo Error 2', 'make: *** No rule to make target ''run_unit_test''.
', '2020-07-06 04:40:55,544 RPL-2.0      INFO     Build Started
2020-07-06 04:40:55,545 RPL-2.0      INFO     Building
2020-07-06 04:40:55,545 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  unit_test.py tiempo.py unit_test_wrapper.py assignment_main.py
2020-07-06 04:40:55,579 RPL-2.0      INFO     end_BUILD
2020-07-06 04:40:55,579 RPL-2.0      INFO     Build Ended
2020-07-06 04:40:55,579 RPL-2.0      INFO     Run Started
2020-07-06 04:40:55,580 RPL-2.0      INFO     Running Unit Tests
2020-07-06 04:40:55,580 RPL-2.0      INFO     start_RUN

2020-07-06 04:40:55,582 RPL-2.0      INFO     end_RUN
2020-07-06 04:40:55,582 RPL-2.0      INFO     RUN ERROR
', '2020-07-06 04:40:57', '2020-07-06 04:40:57');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (4, 12, 1, 'Completed all stages', '', '2020-07-08 00:20:03,807 RPL-2.0      INFO     Build Started
2020-07-08 00:20:03,812 RPL-2.0      INFO     Building
2020-07-08 00:20:03,812 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  tiempo.py assignment_main.py
2020-07-08 00:20:03,881 RPL-2.0      INFO     end_BUILD
2020-07-08 00:20:03,881 RPL-2.0      INFO     Build Ended
2020-07-08 00:20:03,881 RPL-2.0      INFO     Run Started
2020-07-08 00:20:03,891 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 00:20:03,892 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
07:16:04.0000
2020-07-08 00:20:03,930 RPL-2.0      INFO     end_RUN
2020-07-08 00:20:03,930 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 00:20:03,930 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
07:16:05.0000
2020-07-08 00:20:03,938 RPL-2.0      INFO     end_RUN
2020-07-08 00:20:03,938 RPL-2.0      INFO     RUN OK
2020-07-08 00:20:03,938 RPL-2.0      INFO     Run Ended
', '2020-07-08 00:20:04', '2020-07-08 00:20:04');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (5, 21, 0, 'Error en IO TEST: IO_test_0.txt. Codigo Error 2', 'Traceback (most recent call last):
  File "assignment_main.py", line 16, in <module>
    main()
  File "assignment_main.py", line 7, in main
    hs, min, seg = tiempo.segundos_a_horas_minutos_segundos(tiempo_segundos)
TypeError: cannot unpack non-iterable NoneType object
make: *** [run] Error 1
Traceback (most recent call last):
  File "assignment_main.py", line 16, in <module>
    main()
  File "assignment_main.py", line 7, in main
    hs, min, seg = tiempo.segundos_a_horas_minutos_segundos(tiempo_segundos)
TypeError: cannot unpack non-iterable NoneType object
make: *** [run] Error 1
', '2020-07-08 22:05:07,436 RPL-2.0      INFO     Build Started
2020-07-08 22:05:07,440 RPL-2.0      INFO     Building
2020-07-08 22:05:07,440 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  tiempo.py assignment_main.py
2020-07-08 22:05:07,491 RPL-2.0      INFO     end_BUILD
2020-07-08 22:05:07,491 RPL-2.0      INFO     Build Ended
2020-07-08 22:05:07,491 RPL-2.0      INFO     Run Started
2020-07-08 22:05:07,503 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:05:07,503 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
Makefile:8: recipe for target ''run'' failed
2020-07-08 22:05:07,532 RPL-2.0      INFO     end_RUN
2020-07-08 22:05:07,532 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:05:07,533 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
Makefile:8: recipe for target ''run'' failed
2020-07-08 22:05:07,538 RPL-2.0      INFO     end_RUN
2020-07-08 22:05:07,539 RPL-2.0      INFO     RUN ERROR
', '2020-07-08 22:05:08', '2020-07-08 22:05:08');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (6, 22, 1, 'Completed all stages', '', '2020-07-08 22:05:34,812 RPL-2.0      INFO     Build Started
2020-07-08 22:05:34,817 RPL-2.0      INFO     Building
2020-07-08 22:05:34,817 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  tiempo.py assignment_main.py
2020-07-08 22:05:34,864 RPL-2.0      INFO     end_BUILD
2020-07-08 22:05:34,864 RPL-2.0      INFO     Build Ended
2020-07-08 22:05:34,864 RPL-2.0      INFO     Run Started
2020-07-08 22:05:34,870 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:05:34,870 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
07:16:04.0000
2020-07-08 22:05:34,905 RPL-2.0      INFO     end_RUN
2020-07-08 22:05:34,906 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:05:34,906 RPL-2.0      INFO     start_RUN
/usr/bin/python3.7 assignment_main.py
07:16:05.0000
2020-07-08 22:05:34,911 RPL-2.0      INFO     end_RUN
2020-07-08 22:05:34,911 RPL-2.0      INFO     RUN OK
2020-07-08 22:05:34,911 RPL-2.0      INFO     Run Ended
', '2020-07-08 22:05:35', '2020-07-08 22:05:35');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (7, 23, 0, 'Error en Building. Codigo Error 2', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
main.o: In function `main'':
main.c:(.text+0x41): undefined reference to `segundos_a_horas_minutos_segundos''
collect2: error: ld returned 1 exit status
make: *** [build] Error 1
', '2020-07-08 22:19:31,605 RPL-2.0      INFO     Build Started
2020-07-08 22:19:31,607 RPL-2.0      INFO     Building
2020-07-08 22:19:31,607 RPL-2.0      INFO     start_BUILD
gcc    -c -o main.o main.c
gcc  -o main main.o
Makefile:10: recipe for target ''build'' failed
2020-07-08 22:19:31,889 RPL-2.0      INFO     end_BUILD
BUILD ERROR: error_code --> 2
', '2020-07-08 22:19:32', '2020-07-08 22:19:32');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (8, 24, 1, 'Completed all stages', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
', '2020-07-08 22:20:39,951 RPL-2.0      INFO     Build Started
2020-07-08 22:20:39,953 RPL-2.0      INFO     Building
2020-07-08 22:20:39,953 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
gcc    -c -o main.o main.c
gcc  -o main tiempo.o main.o
2020-07-08 22:20:40,049 RPL-2.0      INFO     end_BUILD
2020-07-08 22:20:40,049 RPL-2.0      INFO     Build Ended
2020-07-08 22:20:40,049 RPL-2.0      INFO     Run Started
2020-07-08 22:20:40,055 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:20:40,055 RPL-2.0      INFO     start_RUN
./main
07:16:04.0000
2020-07-08 22:20:40,057 RPL-2.0      INFO     end_RUN
2020-07-08 22:20:40,057 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:20:40,057 RPL-2.0      INFO     start_RUN
./main
07:16:05.0000
2020-07-08 22:20:40,058 RPL-2.0      INFO     end_RUN
2020-07-08 22:20:40,058 RPL-2.0      INFO     RUN OK
2020-07-08 22:20:40,058 RPL-2.0      INFO     Run Ended
', '2020-07-08 22:20:40', '2020-07-08 22:20:40');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (9, 25, 0, 'Error en Building. Codigo Error 2', 'tiempo.c: In function ‘segundos_a_horas_minutos_segundos’:
tiempo.c:20:5: error: invalid operands to binary * (have ‘int’ and ‘int *’)
     *minutos = min;
     ^
make: *** [tiempo.o] Error 1
main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
make: Target ''build'' not remade because of errors.
', '2020-07-08 22:21:52,878 RPL-2.0      INFO     Build Started
2020-07-08 22:21:52,880 RPL-2.0      INFO     Building
2020-07-08 22:21:52,880 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
<builtin>: recipe for target ''tiempo.o'' failed
gcc    -c -o main.o main.c
2020-07-08 22:21:52,938 RPL-2.0      INFO     end_BUILD
BUILD ERROR: error_code --> 2
', '2020-07-08 22:21:53', '2020-07-08 22:21:53');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (10, 26, 0, 'Error en Building. Codigo Error 2', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
main.o: In function `main'':
main.c:(.text+0x41): undefined reference to `segundos_a_horas_minutos_segundos''
collect2: error: ld returned 1 exit status
make: *** [build] Error 1
', '2020-07-08 22:23:16,405 RPL-2.0      INFO     Build Started
2020-07-08 22:23:16,407 RPL-2.0      INFO     Building
2020-07-08 22:23:16,407 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
gcc    -c -o main.o main.c
gcc  -o main tiempo.o main.o
Makefile:10: recipe for target ''build'' failed
2020-07-08 22:23:16,485 RPL-2.0      INFO     end_BUILD
BUILD ERROR: error_code --> 2
', '2020-07-08 22:23:17', '2020-07-08 22:23:17');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (11, 27, 1, 'Completed all stages', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
', '2020-07-08 22:23:32,858 RPL-2.0      INFO     Build Started
2020-07-08 22:23:32,860 RPL-2.0      INFO     Building
2020-07-08 22:23:32,860 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
gcc    -c -o main.o main.c
gcc  -o main tiempo.o main.o
2020-07-08 22:23:32,950 RPL-2.0      INFO     end_BUILD
2020-07-08 22:23:32,950 RPL-2.0      INFO     Build Ended
2020-07-08 22:23:32,950 RPL-2.0      INFO     Run Started
2020-07-08 22:23:32,955 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:23:32,955 RPL-2.0      INFO     start_RUN
./main
07:16:04.0000
2020-07-08 22:23:32,958 RPL-2.0      INFO     end_RUN
2020-07-08 22:23:32,958 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:23:32,958 RPL-2.0      INFO     start_RUN
./main
07:16:05.0000
2020-07-08 22:23:32,959 RPL-2.0      INFO     end_RUN
2020-07-08 22:23:32,959 RPL-2.0      INFO     RUN OK
2020-07-08 22:23:32,959 RPL-2.0      INFO     Run Ended
', '2020-07-08 22:23:33', '2020-07-08 22:23:33');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (12, 28, 1, 'Completed all stages', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
', '2020-07-08 22:47:34,366 RPL-2.0      INFO     Build Started
2020-07-08 22:47:34,368 RPL-2.0      INFO     Building
2020-07-08 22:47:34,368 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
gcc    -c -o main.o main.c
gcc  -o main tiempo.o main.o
2020-07-08 22:47:34,461 RPL-2.0      INFO     end_BUILD
2020-07-08 22:47:34,461 RPL-2.0      INFO     Build Ended
2020-07-08 22:47:34,461 RPL-2.0      INFO     Run Started
2020-07-08 22:47:34,466 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:47:34,467 RPL-2.0      INFO     start_RUN
./main
07:16:04.0000
2020-07-08 22:47:34,469 RPL-2.0      INFO     end_RUN
2020-07-08 22:47:34,469 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:47:34,469 RPL-2.0      INFO     start_RUN
./main
07:16:05.0000
2020-07-08 22:47:34,470 RPL-2.0      INFO     end_RUN
2020-07-08 22:47:34,470 RPL-2.0      INFO     RUN OK
2020-07-08 22:47:34,470 RPL-2.0      INFO     Run Ended
', '2020-07-08 22:47:35', '2020-07-08 22:47:35');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (13, 29, 1, 'Completed all stages', 'main.c: In function ‘main’:
main.c:8:5: warning: implicit declaration of function ‘scanf’ [-Wimplicit-function-declaration]
     scanf ("%d",&tiempo_ingresado);
     ^~~~~
main.c:8:5: warning: incompatible implicit declaration of built-in function ‘scanf’
main.c:8:5: note: include ‘<stdio.h>’ or provide a declaration of ‘scanf’
main.c:15:5: warning: implicit declaration of function ‘printf’ [-Wimplicit-function-declaration]
     printf ("%02d:%02d:%07.4f\\n",hs,min,seg);
     ^~~~~~
main.c:15:5: warning: incompatible implicit declaration of built-in function ‘printf’
main.c:15:5: note: include ‘<stdio.h>’ or provide a declaration of ‘printf’
', '2020-07-08 22:50:11,881 RPL-2.0      INFO     Build Started
2020-07-08 22:50:11,883 RPL-2.0      INFO     Building
2020-07-08 22:50:11,883 RPL-2.0      INFO     start_BUILD
gcc    -c -o tiempo.o tiempo.c
gcc    -c -o main.o main.c
gcc  -o main tiempo.o main.o
2020-07-08 22:50:11,973 RPL-2.0      INFO     end_BUILD
2020-07-08 22:50:11,973 RPL-2.0      INFO     Build Ended
2020-07-08 22:50:11,973 RPL-2.0      INFO     Run Started
2020-07-08 22:50:11,978 RPL-2.0      INFO     IO TEST: IO_test_0.txt
2020-07-08 22:50:11,978 RPL-2.0      INFO     start_RUN
./main
07:16:04.0000
2020-07-08 22:50:11,981 RPL-2.0      INFO     end_RUN
2020-07-08 22:50:11,981 RPL-2.0      INFO     IO TEST: IO_test_1.txt
2020-07-08 22:50:11,981 RPL-2.0      INFO     start_RUN
./main
07:16:05.0000
2020-07-08 22:50:11,982 RPL-2.0      INFO     end_RUN
2020-07-08 22:50:11,982 RPL-2.0      INFO     RUN OK
2020-07-08 22:50:11,982 RPL-2.0      INFO     Run Ended
', '2020-07-08 22:50:12', '2020-07-08 22:50:12');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (14, 30, 0, 'Error en Running Unit Tests. Codigo Error 2', 'make: *** No rule to make target ''run_unit_test''.
', '2020-07-10 20:49:48,459 RPL-2.0      INFO     Build Started
2020-07-10 20:49:48,463 RPL-2.0      INFO     Building
2020-07-10 20:49:48,463 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  unit_test.py tiempo.py unit_test_wrapper.py assignment_main.py
2020-07-10 20:49:48,516 RPL-2.0      INFO     end_BUILD
2020-07-10 20:49:48,516 RPL-2.0      INFO     Build Ended
2020-07-10 20:49:48,516 RPL-2.0      INFO     Run Started
2020-07-10 20:49:48,518 RPL-2.0      INFO     Running Unit Tests
2020-07-10 20:49:48,518 RPL-2.0      INFO     start_RUN

2020-07-10 20:49:48,520 RPL-2.0      INFO     end_RUN
2020-07-10 20:49:48,520 RPL-2.0      INFO     RUN ERROR
', '2020-07-10 20:49:49', '2020-07-10 20:49:49');
INSERT INTO hwiutl5aivbm2dss.test_run (id, activity_submission_id, success, exit_message, stderr,
                                       stdout, date_created, last_updated)
VALUES (15, 39, 0, 'Error en Running Unit Tests. Codigo Error 2', 'make: *** No rule to make target ''run_unit_test''.
', '2020-07-10 21:48:35,872 RPL-2.0      INFO     Build Started
2020-07-10 21:48:35,876 RPL-2.0      INFO     Building
2020-07-10 21:48:35,877 RPL-2.0      INFO     start_BUILD
/usr/bin/python3.7 -m py_compile  unit_test.py tiempo.py unit_test_wrapper.py assignment_main.py
2020-07-10 21:48:35,934 RPL-2.0      INFO     end_BUILD
2020-07-10 21:48:35,934 RPL-2.0      INFO     Build Ended
2020-07-10 21:48:35,934 RPL-2.0      INFO     Run Started
2020-07-10 21:48:35,936 RPL-2.0      INFO     Running Unit Tests
2020-07-10 21:48:35,936 RPL-2.0      INFO     start_RUN

2020-07-10 21:48:35,938 RPL-2.0      INFO     end_RUN
2020-07-10 21:48:35,938 RPL-2.0      INFO     RUN ERROR
', '2020-07-10 21:48:36', '2020-07-10 21:48:36');