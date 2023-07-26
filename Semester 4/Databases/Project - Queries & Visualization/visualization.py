import matplotlib.pyplot as plt
import numpy as np
import pyodbc

server = 'mysqlserver3210122.database.windows.net'
database = 'ExercisesDatabase'
username = 'examiner'
password = 'toExamine23'

connection = pyodbc.connect(f'DRIVER={{ODBC Driver 17 for SQL Server}};SERVER={server};DATABASE={database};UID={username};PWD={password}')
print("Connection established")
cursor = connection.cursor()

#Query 1 - Number of movies per year with budget greater than 1000000
cursor.execute('''SELECT YEAR(m.release_date) AS year, COUNT(*) AS movies_per_year
                  FROM movie m
                  WHERE m.budget > 1000000
                  GROUP BY YEAR(m.release_date)
                  ORDER BY YEAR(m.release_date) ASC;''')

rows = cursor.fetchall()

axisx1 = [rows[i][0] for i in range(0, len(rows))]
axisy1 = [rows[i][1] for i in range(0, len(rows))]

plt.title('Number of movies released per year')
plt.xlabel(cursor.description[0][0])
plt.ylabel(cursor.description[1][0])
plt.xticks(np.arange(min(axisx1) - 1, max(axisx1) + 3, 5))
plt.yticks(np.arange(min(axisy1) - 1, max(axisy1) + 5, 10))
plt.bar(axisx1, axisy1)
plt.show()


#Query 2 - Number of movies belonging to each genre with budget > 1000000 or runtime > 2h 
cursor.execute('''SELECT g.name AS genre, COUNT(*) AS movies_per_genre
                  FROM movie m
                  JOIN hasGenre hg ON hg.movie_id = m.id
                  JOIN genre g ON hg.genre_id = g.id
                  WHERE m.budget > 1000000 OR m.runtime > 120
                  GROUP BY g.name
                  ORDER BY movies_per_genre ASC;''')

rows = cursor.fetchall()

axisx2 = [rows[i][0] for i in range(0, len(rows))]
axisy2 = [rows[i][1] for i in range(0, len(rows))]

plt.title('Number of movies belonging to each genre')
plt.xlabel(cursor.description[0][0])
plt.ylabel(cursor.description[1][0])
plt.xticks(rotation = 45)
plt.yticks(np.arange(min(axisy2) - 6, max(axisy2) + 157, 500))
plt.bar(axisx2, axisy2)
plt.show()


#Query 3 - Number of movies released per year per genre
cursor.execute('''SELECT g.name AS genre , YEAR(m.release_date) AS year, COUNT(*) AS movies_per_gy
                  FROM movie m
                  JOIN hasGenre hg ON hg.movie_id = m.id
                  JOIN genre g ON hg.genre_id = g.id
                  GROUP BY g.name, YEAR(m.release_date)
                  ORDER BY genre ASC, year ASC;''')

rows = cursor.fetchall()

axisx3 = [rows[i][0] for i in range(0, len(rows))]
axisy3 = [rows[i][1] for i in range(0, len(rows))]
axisz3 = [rows[i][2] for i in range(0, len(rows))]

filtered_data = [(x, y, z) for x, y, z in zip(axisx3, axisy3, axisz3) if y is not None]
axisx3, axisy3, axisz3 = zip(*filtered_data)

fig = plt.figure()
ax1 = fig.add_subplot(111, projection='3d')
ax1.set_facecolor((1.0, 1.0, 1.0))
xCategories = axisx3
i = 0
xDict = {}
xl = []
for category in xCategories:
  if category not in xDict:
    xDict[category] = i
    xl.append(i)
    i += 1
  else:
    xl.append(xDict[category])

zl = np.zeros(len(xl))
dx = np.ones(len(xl)) * 0.1
dy = np.ones(len(xl))
dz = axisz3
plt.title('Number of movies per year per genre')
ax1.bar3d(xl, axisy3, zl, dx, dy, dz)
plt.xticks(range(len(xDict.values())), xDict.keys())
ax1.set_ylim3d(min(axisy3) - 2, max(axisy3) + 17, emit = True)
ax1.set_zlim3d(0, max(axisz3) + 50, emit = True)
ax1.set_xlabel(cursor.description[0][0])
ax1.set_ylabel(cursor.description[1][0])
ax1.set_zlabel(cursor.description[2][0])
plt.show()


#Query 4 - Sum of revenues of actor 'Tom Hanks' per year 
cursor.execute('''SELECT YEAR(m.release_date) AS year, SUM(m.revenue) AS revenues_per_year
                  FROM movie m
                  JOIN movie_cast mc ON m.id = mc.movie_id
                  WHERE mc.name = 'Tom Hanks'
                  GROUP BY YEAR(m.release_date)
                  ORDER BY year;''')

rows = cursor.fetchall()

axisx4 = [rows[i][0] for i in range(0, len(rows))]
axisy4 = [rows[i][1] / 1000000 for i in range(0, len(rows))]

plt.title('Revenues per year in {USD mil}')
plt.xlabel(cursor.description[0][0])
plt.ylabel(cursor.description[1][0])
plt.xticks(np.arange(min(axisx4), max(axisx4) + 2, 5))
plt.yticks(np.arange(min(axisy4), max(axisy4) + 30, 100.0))
plt.bar(axisx4, axisy4)
plt.show()


#Query 5 - Max positive budget per year
cursor.execute('''SELECT YEAR(m.release_date) AS year, MAX(m.budget) AS max_budget
                  FROM movie m
                  WHERE m.budget > 0
                  GROUP BY YEAR(m.release_date)
                  ORDER BY year ASC;''')

rows = cursor.fetchall()

axisx5 = [rows[i][0] for i in range(0, len(rows))]
axisy5 = [rows[i][1] / 1000000 for i in range(0, len(rows))]

plt.title('Max budget per year in {USD mil}')
plt.xlabel(cursor.description[0][0])
plt.ylabel(cursor.description[1][0])
plt.xticks(np.arange(min(axisx5) - 2, max(axisx5) + 3, 5))
plt.yticks(np.arange(min(axisy5), max(axisy5) + 1, 25))
plt.scatter(axisx5, axisy5, s = 5, c = 'black')
plt.show()


#Query 7 - Average rating and rating count per user 
cursor.execute('''SELECT AVG(r.rating) AS avg_rating, COUNT(*) AS rating_count
                  FROM ratings r
                  GROUP BY r.user_id;''')

rows = cursor.fetchall()
axisx7 = [rows[i][0] for i in range(0, len(rows))]
axisy7 = [rows[i][1] for i in range(0, len(rows))]

plt.title('Average rating and rating count per user')
plt.xlabel(cursor.description[0][0])
plt.ylabel(cursor.description[1][0])
plt.xticks(np.arange(min(axisx7) - 0.5, max(axisx7) + 0.1, 0.2))
plt.yticks(np.arange(min(axisy7) - 1, max(axisy7) + 35, 25))
plt.scatter(axisx7, axisy7, s = 4, c = 'black')
plt.show()