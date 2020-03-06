#include <stdio.h>
#include <stdlib.h>


int main(void) {
int tiempo_ingresado;
int min, hs;
float seg;

    //printf ("Ingresar una cantidad en segundos.\n");
    scanf ("%d",&tiempo_ingresado);
    //printf ("%d\n",tiempo_ingresado);
    //seg = 3661; Para poner numeros de prueba.
    hs = (int) tiempo_ingresado / 3600;
    tiempo_ingresado = tiempo_ingresado % 3600;
    min = tiempo_ingresado / 60;
    seg = tiempo_ingresado % 60;
    
    printf ("%02d:%02d:%07.4f\n",hs,min,seg);
    
    // No se como darle formato a los valores.
    
   return EXIT_SUCCESS;
}


int foo() {
    printf("Soy foo");
    return 1;
}

int bar() {
    printf("Soy bar");
    return 2;
}
