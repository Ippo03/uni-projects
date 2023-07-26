-- Query 1
SELECT YEAR(m.release_date) AS year, COUNT(*) AS movies_per_year
FROM movie m
WHERE m.budget > 1000000
GROUP BY YEAR(m.release_date)
ORDER BY YEAR(m.release_date) ASC;
-- OUTPUT : 82 rows 

-- Query 2
SELECT g.name AS genre, COUNT(*) AS movies_per_genre
FROM movie m
JOIN hasGenre hg ON hg.movie_id = m.id
JOIN genre g ON hg.genre_id = g.id
WHERE m.budget > 1000000 OR m.runtime > 120
GROUP BY g.name
ORDER BY movies_per_genre ASC;
-- OUTPUT : 9 rows 

-- Query 3
SELECT g.name AS genre , YEAR(m.release_date) AS year, COUNT(*) AS movies_per_gy
FROM movie m
JOIN hasGenre hg ON hg.movie_id = m.id
JOIN genre g ON hg.genre_id = g.id
GROUP BY g.name, YEAR(m.release_date)
ORDER BY genre ASC, year ASC;
-- OUTPUT : 675 rows 

-- Query 4
SELECT YEAR(m.release_date) AS year, SUM(m.revenue) AS revenues_per_year
FROM movie m
JOIN movie_cast mc ON m.id = mc.movie_id
WHERE mc.name = 'Tom Hanks'
GROUP BY YEAR(m.release_date)
ORDER BY year;
-- OUTPUT : 18 rows 

-- Query 5
SELECT YEAR(m.release_date) AS year, MAX(m.budget) AS max_budget
FROM movie m
WHERE m.budget > 0
GROUP BY YEAR(m.release_date)
ORDER BY year ASC;
-- OUTPUT : 90 rows 

-- Query 6
SELECT c.name AS trilogy_name
FROM movie m
JOIN belongsTocollection btc ON m.id = btc.movie_id
JOIN collection c ON collection_id = c.id
GROUP BY c.name
HAVING COUNT(*) = 3
-- OUTPUT 93 rows 

-- Query 7
SELECT AVG(r.rating) AS avg_rating, COUNT(*) AS rating_count
FROM ratings r
GROUP BY r.user_id;
-- OUTPUT 671 rows

-- Query 8
SELECT TOP 10 m.title AS movie_title, m.budget AS budget
FROM movie m
ORDER BY m.budget DESC;
-- OUTPUT 10 rows

-- Query 9
SELECT year, max_budget
FROM (
    SELECT YEAR(m1.release_date) AS year, m1.title AS max_budget, m1.budget,
           ROW_NUMBER() OVER (PARTITION BY YEAR(m1.release_date) ORDER BY m1.budget DESC) AS rank
    FROM movie m1
    JOIN (
        SELECT YEAR(m2.release_date) AS year, MAX(m2.budget) AS max
        FROM movie m2
        WHERE m2.budget > 0
        GROUP BY YEAR(m2.release_date)
    ) AS nested1
    ON YEAR(m1.release_date) = nested1.year AND m1.budget = nested1.max
) AS nested2
WHERE rank = 1
ORDER BY year ASC;
-- OUTPUT 90 rows

-- Query 10
CREATE VIEW Popular_Movie_Pairs AS
SELECT r1.movie_id AS movie_1, r2.movie_id AS movie_2, COUNT(*) AS popularity
FROM ratings r1
JOIN ratings r2 ON r1.user_id = r2.user_id
WHERE r1.movie_id != r2.movie_id AND r1.rating > 4 AND r2.rating > 4
GROUP BY r1.movie_id, r2.movie_id
HAVING COUNT(*) > 10







