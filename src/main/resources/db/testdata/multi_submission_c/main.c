#include <stdlib.h>
#include "tiempo.h"


int main() {
    int tiempo_ingresado;

    scanf ("%d",&tiempo_ingresado);

    int min, hs;
    float seg;

    segundos_a_horas_minutos_segundos(tiempo_ingresado, &hs, &min, &seg);

    printf ("%02d:%02d:%07.4f\n",hs,min,seg);
    
   return EXIT_SUCCESS;
}