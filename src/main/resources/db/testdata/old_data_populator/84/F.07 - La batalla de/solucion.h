//Aqui van tus constantes.

typedef struct ejercito {
    char nombre_reino[MAX_NOMBRE];
    int fuerza_total;
    bool esta_cansado;
} ejercito_t;

int resultado_de_batalla(ejercito_t *ejercito_1, ejercito_t *ejercito_2);