SELECT movie_id, AVG(rating) as Average_Rating
FROM ratings
GROUP BY movie_id
HAVING AVG(rating) > 4;
/*OUTPUT: 205 rows.*/

