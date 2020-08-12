#define MAX_TRASGOS 300

typedef struct trasgo {
    bool derrotado; 
    int cant_disparos; 
    int cant_disparos_errados;
} trasgo_t;

void insertar_trasgo(trasgo_t trasgos[MAX_TRASGOS], size_t* tope_trasgos, bool derrotado, int cant_disparos, int cant_disparos_errados) ;