#include<stdbool.h>

#define MAX_NOMBRE 50

typedef struct guerrero {
    char nombre[MAX_NOMBRE];
    unsigned resistencia;
    unsigned ataque;
    unsigned defensa;
} guerrero_t;

char duelo(guerrero_t un_guerrero, guerrero_t otro_guerrero) ;