#define MAX_FLECHAS 100

typedef struct flecha {
    int resistencia_aerodinamica;
    int dureza_material;
} flecha_t;

void retirar_flecha(flecha_t estuche[MAX_FLECHAS], size_t* tope_flechas) ;