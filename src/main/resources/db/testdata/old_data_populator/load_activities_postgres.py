import psycopg2
import requests

headers = {
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkxMjI0NjI2LCJleHAiOjE1OTEyMzkwMjZ9.Moyl44RMqDTrNxL8u2pJMyz02k6-6GHTXijYXxs41xGAiTg2Db40_wdCObYdbuYGyrwG8wJ9C8R8i3pxAfczTQ'
}

def connect():
  """ Connect to the PostgreSQL database server """
  conn = None
  try:

    # connect to the PostgreSQL server
    print('Connecting to the PostgreSQL database...')
    with psycopg2.connect(host="localhost", database="rpldb", user="postgres",
                          password="postgres") as conn:
      # create a cursor
      with conn.cursor() as cur:
        cur.execute(
          "SELECT * FROM activity WHERE id > 2000 AND state='ENABLED' AND topic_id IS NOT NULL")
        result = cur.fetchall()
        # activity1 = result[0]
        for activity in result:
          id, name, description, language, points, topic_id, test_type, template, input, output, tests, state = activity
          # print(id, "\n====================================================================")
          # print(name, "\n====================================================================")
          # print(description, "\n====================================================================")
          # print(language, "\n====================================================================")
          # print(points, "\n====================================================================")
          # print(topic_id, "\n====================================================================")
          # print(test_type, "\n====================================================================")
          # print(template, "\n====================================================================")
          # print(input, "\n====================================================================")
          # print(output, "\n====================================================================")
          # print(tests, "\n====================================================================")
          # print(state, "\n====================================================================")

          if language == "PYTHON3":
            language = "python"

          my_activity = create_activity(name, description, language, topic_id,
                                        initial_code=template)

          try:
            if test_type == "INPUT":
              test = create_io_test(my_activity["id"], my_activity["course_id"],
                                    input, output)

            if test_type == "TEST":
              tests = tests.replace('#include "solution.c"',
                                    '#include "main.c"')
              test = create_unit_test(my_activity["id"],
                                      my_activity["course_id"], tests)
          except Exception as e:
            print(e)
            print(activity)
            print(my_activity)

          print(f"created Activity {my_activity['id']} and test {test['id']}")


  except (Exception, psycopg2.DatabaseError) as error:
    print(error)


def create_activity(name, description, language, category_id, initial_code):
  backend_url = "http://localhost:8080/api/courses/1/activities"

  payload = {
    'description': description,
    'name': name,
    'language': language,
    'activityCategoryId': category_id,
    'initialCode': initial_code or "nonull"
  }
  files = [
    ('supportingFile', open('./load_activities_postgres.py', 'rb'))
  ]

  response = requests.post(backend_url, headers=headers, data=payload,
                           files=files)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear la activity: {response.json()}")
  return response.json()


def create_io_test(activity_id, course_id, text_in, text_out):
  backend_url = f"http://localhost:8080/api/courses/{course_id}/activities/{activity_id}/iotests"

  payload = {
    'text_in': text_in or "",
    'text_out': text_out,
  }

  response = requests.post(backend_url, headers=headers, json=payload)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear el test IO: {response.json()}")
  return response.json()


def create_unit_test(activity_id, course_id, unit_test_code):
  backend_url = f"http://localhost:8080/api/courses/{course_id}/activities/{activity_id}/unittests"

  payload = {
    'unit_test_code': unit_test_code
  }

  response = requests.post(backend_url, headers=headers, json=payload)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear el test UNIT: {response.json()}")
  return response.json()


if __name__ == '__main__':
  connect()
