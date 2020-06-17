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
        cur.execute("SELECT * FROM topic")
        result = cur.fetchall()
        for category in result:
          id, name, course_id, state = category
          # print(id, "\n====================================================================")
          # print(name, "\n====================================================================")
          # print(state, "\n====================================================================")

          my_category = create_category(name)
          print(f"Created category {my_category['id']}")

  except (Exception, psycopg2.DatabaseError) as error:
    print(error)


def create_category(name):
  backend_url = "http://localhost:8080/api/courses/1/activityCategories"

  payload = {
    'description': name + "no description",
    'name': name,
  }

  response = requests.post(backend_url, headers=headers, json=payload)

  if response.status_code not in [200, 201]:
    raise Exception(f"Error al postear category: {response.json()}")
  return response.json()


if __name__ == '__main__':
  connect()
