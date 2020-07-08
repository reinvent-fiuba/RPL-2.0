import io
import psycopg2
import requests

'''
DOCUMENTATION

Requisitos:
 - DUMP.sql de la base anterior de rpl v1.
 - Postgres 9.5.22 

1. Instalar postgres y crear la DB y el usuario

CREEEO QUE (sudo apt install postgresql-9.5)
sudo -u postgres psql
create user rpl with password 'rpl';
create database rpldb;
grant all privileges on database rpldb to rpl;
\q 


2. Correr la migracion
psql -U rpl -d rpldb -1 -f db.sql.backup.12-06-20.mariano.sql 2> errors.txt

'''


headers = {
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkzODg3MzU1LCJleHAiOjE1OTM5MDE3NTV9.Xg183ArkcyespqSfg1x_6MnO27PiFqRtDiYe2URXaCkA28_sV0ccnnt9h4VgYDKU9F9QYg3UBMN2jsCKkZPDzA'
}

def migrate_categories():
  """ Connect to the PostgreSQL database server """
  conn = None
  old_to_new_ids = {}
  try:

    # connect to the PostgreSQL server
    print('Connecting to the PostgreSQL database...')
    with psycopg2.connect(host="localhost", database="rpldb", user="postgres",
                          password="postgres") as conn:
      # create a cursor
      with conn.cursor() as cur:
        cur.execute("SELECT * FROM topic WHERE course_id=22")
        result = cur.fetchall()
        for category in result:
          id, name, course_id, state = category
          # print(id, "\n====================================================================")
          # print(name, "\n====================================================================")
          # print(state, "\n====================================================================")

          my_category = create_category(name)
          old_to_new_ids[id] = my_category['id']
          print(f"Created category {my_category['id']}")

  except (Exception, psycopg2.DatabaseError) as error:
    print(error)

  return old_to_new_ids


def create_category(name):
  backend_url = "http://localhost:8080/api/courses/2/activityCategories"

  payload = {
    'description': name + "no description",
    'name': name,
  }

  response = requests.post(backend_url, headers=headers, json=payload)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear category: {response.json()}")
  return response.json()


def migrate_activities(old_to_new_category_ids):
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
          "SELECT * FROM activity WHERE topic_id in (SELECT id FROM topic WHERE course_id=22) AND state='ENABLED'")
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

          my_activity = create_activity(name, description, language,
                                        old_to_new_category_ids[topic_id],
                                        points,
                                        initial_code=template)
          activate_activity(my_activity['id'])


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


def activate_activity(id):
  backend_url = f"http://localhost:8080/api/courses/2/activities/{id}"
  response = requests.patch(backend_url, headers=headers, data={'active': True},
                            files={'wo': b'lal'})
  if response.status_code not in [200, 201]:
    raise Exception(f"Error al activar la activity: {response.json()}")
  return response.json()


def create_activity(name, description, language, category_id, points,
    initial_code):
  backend_url = "http://localhost:8080/api/courses/2/activities"

  payload = {
    'description': description,
    'name': name,
    'points': points,
    'language': language,
    'activityCategoryId': category_id,
  }

  filename = "main.c" if language.lower() == "c" else "assignment_main.py"

  with io.FileIO(filename, 'wb+') as startingFile:
    startingFile.write(str.encode(initial_code or "no_code"))
    startingFile.seek(0)
    files = [
      ('startingFile', startingFile)
    ]

    response = requests.post(backend_url, headers=headers, data=payload,
                             files=files)

    if response.status_code not in [200, 201]:
      raise Exception(f"Error al postear la activity: {response.json()}")
    return response.json()


def create_io_test(activity_id, course_id, text_in, text_out):
  backend_url = f"http://localhost:8080/api/courses/{course_id}/activities/{activity_id}/iotests"

  payload = {
    'name': "IO Test migrado",
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
    'unit_test_code': unit_test_code + '\n\n\n'
  }

  response = requests.post(backend_url, headers=headers, json=payload)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear el test UNIT: {response.json()}")
  return response.json()


if __name__ == '__main__':
  category_ids = migrate_categories()
  migrate_activities(category_ids)
