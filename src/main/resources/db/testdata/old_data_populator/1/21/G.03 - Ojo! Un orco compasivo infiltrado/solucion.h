#define MAX_ORCOS 70

typedef struct orco {
    int fuerza;
    int compasion;
} orco_t;

bool eliminar_orco_compasivo(orco_t orcos[MAX_ORCOS], size_t* tope_orcos, int compasion);