def segundos_a_horas_minutos_segundos(tiempo_segundos):
  hs = tiempo_segundos // 3600
  tiempo_segundos = tiempo_segundos % 3600
  min = tiempo_segundos // 60
  seg = tiempo_segundos % 60

  return hs, min, seg
