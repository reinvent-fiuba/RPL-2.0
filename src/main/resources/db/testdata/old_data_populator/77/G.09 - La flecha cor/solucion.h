#define MAX_FLECHAS 100

typedef struct flecha {
    int resistencia_aerodinamica;
    int dureza_material;
} flecha_t;

flecha_t buscar_flecha_correcta(flecha_t estuche[MAX_FLECHAS], size_t *tope_flechas, int fuerza_enemigo) ;