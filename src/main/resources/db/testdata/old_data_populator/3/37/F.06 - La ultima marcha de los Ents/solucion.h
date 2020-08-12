//Aqui van tus constantes.

#define MAX_NOMBRE 50 
#define MAX_ARBOLES 1000
#define MAX_ORCOS   1000


typedef struct ent {
    char nombre[MAX_NOMBRE];
    int factor_liderazgo;
} ent_t;

typedef struct bosque {
    ent_t lider;
    int arboles[MAX_ARBOLES];
    int cantidad_de_arboles;
} bosque_t;

typedef struct batallon {
    int orcos[MAX_ORCOS];
    int cantidad_de_orcos;
    bool es_piromaniaco;
} batallon_t;


int resultado_de_batalla(bosque_t bosque, batallon_t batallon);