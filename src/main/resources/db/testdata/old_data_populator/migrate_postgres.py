import io
import json
from pathlib import Path

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
    'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTk2MTE3MzY4LCJleHAiOjE1OTYxMzE3Njh9.jD50lAZRbdc66Xlto7fSFNGCjvPpDhAbs6aVTYlbOMlrOlTO4oMbR5FphC0uqHR6oFWTx8Q8R63Hr2XnIi_4vw'
}

# headers = {
#     'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTk2MTE3NTg4LCJleHAiOjE1OTYxMzE5ODh9.zB8N7m2pAR9d_bxPLRU0d0vdx-OO4g4k5d2YXhpV1uJPiMD2cWSlXAE8ghrdZEdcVjmMRaK_JyJENA2EX6Vj-g'
#
# }


# base_url = "https://enigmatic-bayou-58033.herokuapp.com"
# base_url = "http://localhost:8080"
base_url = "http://www.rpl.codes"
migration_course_id = 3


def migrate_categories():
    """ Connect to the PostgreSQL database server """
    conn = None
    old_to_new_ids = {}
    try:

        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        with psycopg2.connect(host="localhost", database="rpldb",
                              user="postgres",
                              password="postgres") as conn:
            # create a cursor
            with conn.cursor() as cur:
                cur.execute("SELECT * FROM topic WHERE course_id=22")
                result = cur.fetchall()
                for category in result:
                    id, name, course_id, state = category
                    # print(id, "\n===========================================")
                    # print(name, "\n=========================================")
                    # print(state, "\n========================================")

                    my_category = create_category(name)
                    old_to_new_ids[id] = my_category['id']
                    print(f"Created category {my_category['id']}")

    except (Exception, psycopg2.DatabaseError) as error:
        print(error)

    return old_to_new_ids


def create_category(name):
    backend_url = f"{base_url}/api/courses/{migration_course_id}/activityCategories"

    payload = {
        'description': name + " no description",
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
        with psycopg2.connect(host="localhost", database="rpldb",
                              user="postgres",
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

                    try:
                        my_activity = create_activity(name, description,
                                                      language,
                                                      old_to_new_category_ids[
                                                          topic_id],
                                                      points,
                                                      template,
                                                      test_type)
                        activate_activity(my_activity['id'])


                        if test_type == "INPUT":
                            test = create_io_test(my_activity["id"],
                                                  my_activity["course_id"],
                                                  input, output)

                        if test_type == "TEST":
                            tests = tests.replace('#include "solution.c"',
                                                  '#include "solucion.h"')
                            test = create_unit_test(my_activity["id"],
                                                    my_activity["course_id"],
                                                    tests)

                        print(
                            f"created Activity {my_activity['id']} and test {test['id']}")

                    except Exception as e:
                        print(e)
                        print(activity)
                        print(my_activity)



    except (Exception, psycopg2.DatabaseError) as error:
        print(error)


def activate_activity(id):
    backend_url = f"{base_url}/api/courses/{migration_course_id}/activities/{id}"
    response = requests.patch(backend_url, headers=headers,
                              data={'active': True},
                              files={'wo': b'lal'})
    if response.status_code not in [200, 201]:
        raise Exception(f"Error al activar la activity: {response.json()}")
    return response.json()


def create_activity(name, description, language, category_id, points, initial_code, test_type):
    backend_url = f"{base_url}/api/courses/{migration_course_id}/activities"

    payload = {
        'description': description,
        'name': name,
        'points': points,
        'language': language,
        'activityCategoryId': category_id,
        'compilationFlags': '-g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast'
    }

    filename = "main.c" if language.lower() == "c" else "assignment_main.py"

    if test_type == "INPUT" or language.lower() != "c":
        with io.FileIO(filename, 'wb+') as startingFile:
            startingFile.write(str.encode(initial_code or "no_code"))
            startingFile.seek(0)
            files = [
                ('startingFile', startingFile)
            ]

            response = requests.post(backend_url, headers=headers, data=payload,
                                     files=files)

            if response.status_code not in [200, 201]:
                raise Exception(
                    f"Error al postear la activity: {response.json()}")
            return response.json()

    if test_type == "TEST":
        path = Path(f"./{migration_course_id}/{category_id}/{name}")
        path.mkdir(parents=True,exist_ok=True)
        with open(path / "description.md", 'w+') as description_f:
            description_f.write(description)
        # with io.FileIO("solucion.c", 'wb+') as mainC, io.FileIO("solucion.h", 'wb+') as mainH, open("files_metadata",'wb+') as files_metadata:
        with open(path / "solucion.c", 'w+') as mainC, open(path / "solucion.h",'w+') as mainH, open(path / "files_metadata",'w+') as files_metadata:

            lines = initial_code.split("\n")


            # Everything I add to solution.h,  I remove from solution.c
            function_firm = ""
            for i, line in enumerate(lines):
                if "{" in line and "struct" not in line:
                    function_firm += line.replace("{", ";")
                    break
                function_firm += line + "\n"

            # mainH.write(str.encode(function_firm))
            mainH.write(function_firm)
            mainH.seek(0)

            initial_code = "\n".join(lines[i:])
            initial_code = '#include "solucion.h"\n' + initial_code
            # mainC.write(str.encode(initial_code or "no_code"))
            mainC.write(initial_code or "no_code")
            mainC.seek(0)

            # files_metadata.write(str.encode(json.dumps({"solucion.c": "read_write", "solucion.h": "read"}))
            files_metadata.write(json.dumps({"solucion.c": {"display": "read_write"}, "solucion.h": {"display": "read_write"}}))
            files_metadata.seek(0)

            files = [
                ('startingFile', mainC),
                ('startingFile', mainH),
                ('startingFile', files_metadata)
            ]

            response = requests.post(backend_url, headers=headers, data=payload,
                                     files=files)

            if response.status_code not in [200, 201]:
                raise Exception(
                    f"Error al postear la activity {name}: {response.json()}")
            return response.json()


def create_io_test(activity_id, course_id, text_in, text_out):
    backend_url = f"{base_url}/api/courses/{course_id}/activities/{activity_id}/iotests"

    payload = {
        'name': "IO Test",
        'text_in': text_in or "",
        'text_out': text_out,
    }

    response = requests.post(backend_url, headers=headers, json=payload)

    if response.status_code not in [200, 201]:
        raise Exception(f"Error al postear el test IO: {response.json()}")
    return response.json()


def create_unit_test(activity_id, course_id, unit_test_code):
    backend_url = f"{base_url}/api/courses/{course_id}/activities/{activity_id}/unittests"

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
