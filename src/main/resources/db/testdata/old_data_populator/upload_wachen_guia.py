from migrate_postgres import create_category, create_activity, activate_activity

GUIAS = ["1_conceptos.tex", "13_excepciones.tex", "17_PilasColas.tex",
         "20_ordenamiento_recursivo.tex", "5_ciclos.tex", "9_diccionarios.tex",
         "10_contratos.tex", "14_objetos.tex", "18_Modelo_de_ejecucion.tex",
         "2_programacion.tex", "6_cadenas.tex",
         "11_archivos.tex", "15_herencia_polimorf.tex", "19_ordenamiento.tex",
         "3_funciones.tex", "7_tuplas_y_listas.tex",
         "12_procesamiento.tex", "16_Listas.tex",
         "4_decisiones.tex", "8_busqueda.tex", "ejercicios_c.tex"
         ]
GUIAS.sort()

initial_code = '''def main():
  # codigo del alumne

main()
'''

c_initial_code = '''#include <stdio.h>

int main(){
    // codigo del alumne
}
'''


def main():
  for guia in GUIAS:
    if guia != "ejercicios_c.tex":
      num = int(guia.split("_")[0])
      cat_name = f"{num:02d}_{guia.split('_')[1]}".rstrip(".tex")
      language = "python"
      code = initial_code
    else:
      cat_name = f"{21}_ejercicios_c"
      language = "c"
      code = c_initial_code

    print(f"Guia " + guia)
    print(cat_name)
    categrory = create_category(cat_name)
    cat_id = categrory['id']
    with open('/home/alepox/Desktop/wachen/algo1/apunte/' + guia) as guia_f:
      for i, ej in enumerate(guia_f.read().split("\\begin{ejercicio}")[1:]):
        print(f"Ejercicio {i + 1}")
        ej = ej.lstrip("\n")
        ej = ej.rstrip("\n")
        # ej = ej.rstrip("\\end{ejercicio}")
        ej = ej[:ej.index("\\end{ejercicio}")]
        print(len(ej))

        my_activity = create_activity(f"Ejercicio {i + 1:02d}", ej, language,
                                      cat_id,
                                      10,
                                      initial_code=code)
        activate_activity(my_activity['id'])

        print("SUCCESSSS")

main()
