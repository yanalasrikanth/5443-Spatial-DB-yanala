#Create a database with earthquakes as dataset
CREATE TABLE public.earthquakes 
(earthquake_id int,
 occurred_on date,
 geom geometry,
 latitude numeric,
 longitude numeric,
 depth numeric,
 magnitude numeric,
 calculation_method character varying(10),
 network_id character varying(15),
 place character varying(50),
 cause character varying(12)
)
Update earthquakes SET geom= ST_Point(longitude, latitude);



#Api code in Pyhton 
from flask import Flask
import psycopg2

app = Flask(__name__)

#Home page
@app.route('/')
def hello():
    return "<h1>SPATIAL DATABASE PROJECT</h1><h3>Select one \n 1.FindAll \n 2.FindOne \n 3.FindClosest "

#Find All
@app.route("/FindAll")
def FindAll():
    cursor.execute('select * from public.earthquakes')
    res = cursor.fetchall()
    return res

#Find closest 
@app.get('/FindClosest/<latitude>/<longitude>')
def Find_closest(latitude, longitude):
    com = '''SELECT *, ST_Distance(ST_MakePoint(%s,%s), geom) AS dist FROM earthquakes
    ORDER BY dist LIMIT 2;'''%(latitude, longitude)
    cursor.execute(com)
    res = cursor.fetchall()
    return res

#Find One
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
