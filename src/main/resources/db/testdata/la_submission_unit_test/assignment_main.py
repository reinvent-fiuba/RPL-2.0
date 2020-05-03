def main():
  tiempo_segundos = int(input())

  hs = tiempo_segundos // 3600
  tiempo_segundos = tiempo_segundos % 3600
  min = tiempo_segundos // 60
  seg = tiempo_segundos % 60

  # print ("%02d:%02d:%07.4f\n",hs,min,seg);

  print(
    "{horas:0>2}:{minutos:0>2}:{segundos:07.4f}".format(horas=hs, minutos=min,
                                                        segundos=seg))


def foo():
  print("hola soy foo")
  return 1


def bar():
  print("hola soy bar")
  return 2
