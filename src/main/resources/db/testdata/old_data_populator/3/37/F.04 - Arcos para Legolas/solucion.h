//Aqui van tus constantes.

typedef struct armamento {
    int barriles_armas[MAX_BARRILES];
    int barriles_arcos[MAX_BARRILES];
    int barriles_llenos_armas;
    int barriles_llenos_arcos;
}armamento_t;

int inventario_de_armamento(armamento_t *armamento,int cantidad_espadas,int cantidad_arcos);