import tiempo


def main():
  tiempo_segundos = int(input())

  hs, min, seg = tiempo.segundos_a_horas_minutos_segundos(tiempo_segundos)

  # print ("%02d:%02d:%07.4f\n",hs,min,seg);

  print(
    "{horas:0>2}:{minutos:0>2}:{segundos:07.4f}".format(horas=hs, minutos=min,
                                                        segundos=seg))


main()
