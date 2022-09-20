-- Table: public.earthquakes

-- DROP TABLE IF EXISTS public.earthquakes;

CREATE TABLE IF NOT EXISTS public.earthquakes
(
    earthquake_id integer,
    occurred_on date,
    geom geometry,
    latitude numeric,
    longitude numeric,
    depth numeric,
    magnitude numeric,
    calculation_method character varying(100) COLLATE pg_catalog."default",
    network_id character varying(100) COLLATE pg_catalog."default",
    place character varying(100) COLLATE pg_catalog."default",
    cause character varying(100) COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.earthquakes
    OWNER to postgres;


#pyhton file
from flask import Flask
import psycopg2

app = Flask(__name__)


@app.route('/')
def hello():
    return "<h1>SPATIAL DATABASE PROJECT</h1><h3>Select one \n 1.FindAll \n 2.FindOne \n 3.FindClosest "


@app.route("/FindAll")
def FindAll():
    cursor.execute('select * from public.earthquakes')
    res = cursor.fetchall()
    return res


@app.get('/FindClosest/<latitude>/<longitude>')
def Find_closest(latitude, longitude):
    com = '''SELECT *, ST_Distance(ST_MakePoint(%s,%s), geom) AS dist FROM earthquakes
    ORDER BY dist LIMIT 2;'''%(latitude, longitude)
    cursor.execute(com)
    res = cursor.fetchall()
    return res


@app.get('/FindOne/<earthquake_id>')
def FindOne(earthquake_id):
    com = 'select * from public.earthquakes where earthquake_id= ''' + earthquake_id + ''
    cursor.execute(com)
    res = cursor.fetchall()
    return res


con = psycopg2.connect(database="Project 1", user="postgres", password='123')
cursor = con.cursor()

if __name__ == '__main__':
    app.run(debug=True, port='8080')

