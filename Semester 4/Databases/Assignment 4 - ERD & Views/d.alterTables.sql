ALTER TABLE dbo.hasGenre 
    ADD CONSTRAINT PK_hasG PRIMARY KEY (movie_id, genre_id);

ALTER TABLE dbo.hasKeyword
    ADD CONSTRAINT PK_hasK PRIMARY KEY (movie_id, key_id);

ALTER TABLE dbo.belongsTocollection
    ADD CONSTRAINT PK_belongsToC PRIMARY KEY (movie_id, collection_id);

ALTER TABLE dbo.hasProductioncompany
    ADD CONSTRAINT PK_hasPC PRIMARY KEY (movie_id, pc_id);

ALTER TABLE dbo.ratings 
 ADD CONSTRAINT PK_ratings PRIMARY KEY(user_id, movie_id)