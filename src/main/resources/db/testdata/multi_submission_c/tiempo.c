#include <stdlib.h>
#include "tiempo.h"


int segundos_a_horas_minutos_segundos(int tiempo_ingresado, int *horas, int *minutos, float *segundos) {
    int min, hs;
    float seg;

    hs = (int) tiempo_ingresado / 3600;
    tiempo_ingresado = tiempo_ingresado % 3600;
    min = tiempo_ingresado / 60;
    seg = tiempo_ingresado % 60;
    
//    printf ("%02d:%02d:%07.4f\n",hs,min,seg);


    *horas = hs;
    *minutos = min;
    *segundos = seg;
    
   return 1;
}