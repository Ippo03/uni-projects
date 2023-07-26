-- PRIMARY KEYS

ALTER TABLE dbo.movie
    ADD CONSTRAINT MoviedID PRIMARY KEY (id);

ALTER TABLE dbo.genre
    ADD CONSTRAINT GenreID PRIMARY KEY (id);

ALTER TABLE dbo.productioncompany
    ADD CONSTRAINT PCID PRIMARY KEY (id)
    
ALTER TABLE dbo.collection
    ADD CONSTRAINT ColID PRIMARY KEY (id);

ALTER TABLE dbo.movie_cast
    ADD CONSTRAINT CastID PRIMARY KEY (cid)
    
ALTER TABLE dbo.movie_crew
    ADD CONSTRAINT CrewID PRIMARY KEY (cid)
  
ALTER TABLE dbo.Keyword
    ADD CONSTRAINT KID PRIMARY KEY (id)

-- FOREIGN KEYS

ALTER TABLE dbo.belongsToCollection
    ADD CONSTRAINT fColID FOREIGN KEY (collection_id) REFERENCES dbo.collection(id)
ALTER TABLE dbo.belongsToCollection
    ADD CONSTRAINT fMovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)
 
ALTER TABLE dbo.hasKeyword
    ADD CONSTRAINT fKID FOREIGN KEY (key_id) REFERENCES dbo.Keyword(id)
ALTER TABLE dbo.hasKeyword
    ADD CONSTRAINT f1MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)

ALTER TABLE dbo.hasGenre
    ADD CONSTRAINT fGenreID FOREIGN KEY (genre_id) REFERENCES dbo.genre(id)
ALTER TABLE dbo.hasGenre
    ADD CONSTRAINT f2MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)
 
ALTER TABLE dbo.hasProductioncompany
    ADD CONSTRAINT f3MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)
ALTER TABLE dbo.hasProductioncompany
    ADD CONSTRAINT fPCID FOREIGN KEY (pc_id) REFERENCES dbo.productioncompany(id)

ALTER TABLE dbo.ratings
    ADD CONSTRAINT f4MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)

ALTER TABLE dbo.movie_cast
    ADD CONSTRAINT f5MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)
    
ALTER TABLE dbo.movie_crew
    ADD CONSTRAINT f6MovieID FOREIGN KEY (movie_id) REFERENCES dbo.movie(id)
