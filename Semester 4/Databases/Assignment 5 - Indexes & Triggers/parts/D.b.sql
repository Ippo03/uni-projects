UPDATE movie
SET avg_rating = (
    SELECT AVG(rating)
    FROM ratings
    WHERE ratings.movie_id = movie.id
    GROUP BY ratings.movie_id
);
