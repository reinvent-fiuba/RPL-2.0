# Flags del compilador y de Valgrind.
CC := gcc
CFLAGS := -g -O2 -std=c99 -Wall -Wformat=2 -lm
CFLAGS += -Wshadow -Wpointer-arith -Wunreachable-code
CFLAGS += -Wconversion -Wno-sign-conversion -Wbad-function-cast
VALGRIND = valgrind --leak-check=full --track-origins=yes --show-reachable=yes --error-exitcode=2

ARCHIVOS = $(wildcard *.c)
ARCHIVOS_SIN_UNIT_TEST := $(filter-out unit_test.c, $(ARCHIVOS))
ARCHIVOS_SIN_MAIN := $(filter-out main.c, $(ARCHIVOS))

build: $(ARCHIVOS:.c=.o)
	$(CC) -o main $^

run:
	./main

build_pre_unit_test: $(ARCHIVOS_SIN_UNIT_TEST:.c=.o)
	$(CC) -c $^
	rm *.o

build_unit_test: $(ARCHIVOS_SIN_MAIN:.c=.o)
	$(CC) -o main $^ -lcriterion

run_unit_test:
	./main --json=unit_test_results_output.json --quiet --always-succeed

.PHONY: main clean