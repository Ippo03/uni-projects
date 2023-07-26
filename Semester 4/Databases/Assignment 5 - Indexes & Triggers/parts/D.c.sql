CREATE TRIGGER update_avg_rating_trigger
ON ratings
AFTER INSERT
AS
BEGIN
    -- Calculate the average rating for the specific movie
    UPDATE Movie
    SET avg_rating = (
        SELECT AVG(rating)
        FROM ratings
        WHERE movie_id = inserted.movie_id
        GROUP BY movie_id
    )
    FROM inserted
    WHERE movie.id = inserted.movie_id;
END;
