/* Creating index for movie table */
CREATE INDEX movie_title_idx ON movie (title);

/* Creating index for movie_cast table */
CREATE INDEX movie_cast_movie_id_idx ON movie_cast (movie_id);
