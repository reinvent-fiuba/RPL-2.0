#define MAX_ORCOS 150

typedef struct orco {
    int fuerza;
    int compasion;
} orco_t;

int insertar_orco(orco_t orcos[MAX_ORCOS], size_t* tope_orcos, int fuerza, int compasion) ;