import csv
import ast

#split 'keywords.csv' and remove duplicates -> 66 duplicates
with open('keywords.csv', 'r', encoding='UTF-8') as file:
    reader = csv.reader(file)
    header = next(reader)
    rows1 = []
    rows2 = []
    for row in reader:
        movie_id = int ((row[0].split(','))[0])
        jSon = (ast.literal_eval(row[1]))
        for i in jSon:
            key_id = i.get('id')
            rows1.append( (movie_id,key_id) )
            name = i.get('name')
            rows2.append( (key_id, name))
        
rows1 = list(set(rows1))
rows2 = list(set(rows2))

with open('hasKeyword.csv', 'w', encoding='UTF-8', newline="") as file:
    writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['movie_id', 'key_id'])
    for row in rows1:
        writer.writerow(row)

with open('Keyword.csv', 'w', encoding='UTF-8', newline="") as file:
    writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['id', 'name'])
    for row in rows2:
        writer.writerow(row)

#remove duplicates of 'hasProductioncompany.csv' -> 38 rows
with open('hasProductioncompany.csv', 'r', encoding='UTF-8', newline="") as file:
    reader = csv.reader(file)
    header = next(reader)
    rows3 = []
    for line in reader:
        movie_id, pc_id = line[0], line[1]
        rows3.append((movie_id,pc_id))
    
rows3 = list(set(rows3))

with open('hasProductioncompany.csv', 'w', encoding='UTF-8', newline="") as file:
    writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['movie_id','pc_id'])
    for row in rows3:
        writer.writerow(row)   

#remove duplicates of 'hasGenre.csv' -> 24 rows
with open('hasGenre.csv', 'r', encoding='UTF-8', newline="") as file:
    reader = csv.reader(file)
    header = next(reader)
    rows4 = []
    for line in reader:
        movie_id, genre_id = line[0], line[1]
        rows4.append((movie_id, genre_id))

rows4 = list(set(rows4))

with open('hasGenre.csv', 'w', encoding='UTF-8', newline="") as file:
    writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['movie_id','genre_id'])
    for row in rows4:
        writer.writerow(row)  

#remove duplicates of 'belongsTocollection.csv' -> 2 rows
with open('belongsTocollection.csv', 'r', encoding='UTF-8', newline="") as file:
    reader = csv.reader(file)
    header = next(reader)
    rows5 = []
    for line in reader:
        movie_id, collection_id = line[0], line[1]
        rows5.append((movie_id, collection_id))

rows5 = list(set(rows5))

with open('belongsTocollection.csv', 'w', encoding='UTF-8', newline="") as file:
    writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    writer.writerow(['movie_id','collection_id'])
    for row in rows5:
        writer.writerow(row)  