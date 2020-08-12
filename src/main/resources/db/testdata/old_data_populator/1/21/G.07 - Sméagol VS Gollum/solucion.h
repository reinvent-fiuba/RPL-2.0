#define MAX_PENSAMIENTOS 100

typedef struct pensamiento {
    char duenio; //S si pertenece a Sméagol o G a Gollum
    int intensidad;
} pensamiento_t;

void ordenar_pensamientos(pensamiento_t cabeza[MAX_PENSAMIENTOS], size_t tope) ;