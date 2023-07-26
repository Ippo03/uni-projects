-- 1ST BULLET
--Find person_ids with different name or gender in Person view(Before updating we found 3 person_id in Person with different name or gender)
SELECT person_id 
FROM Person
GROUP BY person_id
HAVING COUNT(*) > 1;

-- 2ND BULLET
--Update name and gender for person_id records in movie_cast(found 1 person_id with different name or value) -> with MIN function we only choose one(the smallest one)
--Change only movie_cast and movie_crew
UPDATE movie_cast
SET name = (
  SELECT MIN(name)
  FROM Person
  WHERE person_id = movie_cast.person_id
),
gender = (
  SELECT MIN(gender)
  FROM Person
  WHERE person_id = movie_cast.person_id
)
WHERE person_id IN (
  SELECT person_id
  FROM Person
  GROUP BY person_id
  HAVING COUNT(DISTINCT name) > 1 OR COUNT(DISTINCT gender) > 1
);

--Update name and gender for person_id records in movie_crew(found 2 person_id with different name or value) -> with MIN function we only choose one(the smallest one)
UPDATE movie_crew
SET name = (
  SELECT MIN(name)
  FROM Person
  WHERE person_id = movie_crew.person_id
),
gender = (
  SELECT MIN(gender)
  FROM Person
  WHERE person_id = movie_crew.person_id
)
WHERE person_id IN (
  SELECT person_id
  FROM Person
  GROUP BY person_id
  HAVING COUNT(DISTINCT name) > 1 OR COUNT(DISTINCT gender) > 1
);



