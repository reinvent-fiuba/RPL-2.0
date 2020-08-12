#define MAX_INGREDIENTES 10

typedef struct ingrediente {
    char codigo;
    unsigned int cantidad;
} ingrediente_t;

bool preparar_antidoto(ingrediente_t ingredientes[MAX_INGREDIENTES], size_t* tope_ingredientes) ;