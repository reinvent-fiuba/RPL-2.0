#define MAX_GUERREROS 35
#define MAX_NOMBRE 50

typedef struct guerrero {
    char nombre_completo[MAX_NOMBRE];
    unsigned cantidad_batallas;
    unsigned edad;
    char insignia;
} guerrero_t;

void buscar_guerrero(guerrero_t guerreros[MAX_GUERREROS], size_t tope_guerreros, char nombre_completo[MAX_NOMBRE]);