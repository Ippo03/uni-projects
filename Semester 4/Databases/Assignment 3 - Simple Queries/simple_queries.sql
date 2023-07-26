-- SIMPLE QUERIES -- 

-- 1
/*
Βρες μου τους τίτλους των ταινιών με μέσο όρο βαθμολογίας από
χρήστες μεγαλύτερο του 25 (με άριστα το 50 !), μαζί με τον μέσο όρο βαθμολογίας τους
OUTPUT: Title, avgRating ->  rows 1302
*/
SELECT m.title as Title, avg(r.rating) as avgRating
FROM movie m
INNER JOIN ratings r
ON m.id = r.movie_id
GROUP BY m.id, m.title
HAVING avg(r.rating) > 25

-- 2
/*
Βρες μου το πλήθος των ταινιών στις οποίες έχει συμμετάσχει κάθε ηθοποιός του πίνακα movie_cast σε φθίνουσα σειρά
OUTPUT: Name, Number_of_Movies ->  69009 rows
*/
SELECT DISTINCT c.name as Name, COUNT(m.title) as Number_of_Movies
FROM movie_cast c
INNER JOIN movie m
ON c.movie_id = m.id
GROUP BY c.name
ORDER BY Number_of_Movies DESC 

-- 3
/*
Βρες μου τους τίτλους των ταινιών με χρονολογία παραγωγής μεταξύ 1970 - 2005
οι οποίες ανήκουν στο είδος Adventure από την πιο παλιά στην πιο πρόσφατη
OUTPUT: Name, Year -> 850 rows
*/
SELECT m.title as Title, YEAR(m.release_date) as Year
FROM movie m
INNER JOIN hasGenre hG
ON m.id = hG.movie_id
INNER JOIN genre g
ON g.id = hG.genre_id
WHERE YEAR(m.release_date) BETWEEN 1970 AND 2005 AND g.name = 'Adventure'
ORDER BY Year ASC

-- 4
/*
Βρες μου τον τίτλο της ταινίας με το μεγαλύτερο κέρδος (revenue - budget)
OUTPUT: Title, Revenue, Budget, Profit -> 1 row
*/
SELECT m.title as Title, m.revenue as Revenue, m.budget as Budget , m.revenue - m.budget as Profit
FROM movie m
WHERE (revenue - budget) = (SELECT MAX(revenue - budget) FROM movie);

-- 5
/*
Βρες μου τους τίτλους των πρώτων 100 ταινιών με το μικρότερο κέρδος (revenue - budget)
OUTPUT: Title, Revenue, Budget, Profit -> 100 rows
*/
SELECT TOP 100 m.title as Title, m.revenue as Revenue, m.budget as Budget , m.revenue - m.budget as Profit
FROM movie m
ORDER BY m.revenue - m.budget ASC

-- 6
/*
Βρες μου τους τίτλους των ταινιών και τα production companies που συμμετέχουν σε αυτές
OUTPUT: 8446 rows
*/
SELECT m.title AS Title, STRING_AGG(pc.name, ',') AS Production_Companies
FROM movie m
INNER JOIN hasProductioncompany hPC 
ON m.id = hPC.movie_id
INNER JOIN productioncompany pc 
ON hPC.pc_id = pc.id
GROUP BY m.title;

-- 7
/*
Βρες σε ποιο collection ανήκει η κάθε ταινία (αν δεν ανήκει σε κανένα collection, επέστρεψε NULL)
OUTPUT: 9994 rows
*/
SELECT m.title as Title, c.name as Collection
FROM movie m
LEFT OUTER JOIN belongsTocollection bTc
ON m.id = bTC.movie_id
LEFT OUTER JOIN collection c
ON bTc.collection_id = c.id

-- 8
/*
Βρες όλα τα είδη στα οποία ανήκει η κάθε ταινία (αν δεν ανήκει σε κανένα είδος, επέστρεψε NULL)
OUTPUT: 9720 rows
*/
SELECT m.title as Title, STRING_AGG(g.name, ',') as Genres
FROM movie m
LEFT OUTER JOIN hasGenre hG
ON m.id = hG.movie_id
LEFT OUTER JOIN genre g
ON hG.genre_id = g.id
GROUP BY m.title
 
-- 9
/*
Βρες τους τίτλους των ταινιών που έχουν στο tagline ή στο overview τους τουλάχιστον ένα από τα keywords τους 
και επέστρεψε το μικρότερο αλφαβητικά keyword. (Με αυτό το τρόπο επιτυγχάνουμε να επιστρέφουμε την κάθε ταινία μία μόνο φορά.)
OUTPUT: 5729 rows
*/
SELECT m.title as Title, m.tagline as Tagline, m.overview as Overview, MIN(k.name) as keyword -- MIN or MAX -> Returns the same rows (with different keyword in some movies)
FROM movie m
INNER JOIN hasKeyword hK ON m.id = hK.movie_id
INNER JOIN Keyword k ON hK.key_id = k.id 
WHERE m.tagline LIKE CONCAT('%', k.name, '%') OR m.overview LIKE CONCAT('%', k.name, '%')
GROUP BY m.title, m.tagline, m.overview

-- 10
/*
Βρες την ελάχιστη βαθμολογία που έχει πάρει η κάθε ταινία από όλους 
τους χρήστες. (Αν δεν έχει βαθμολογηθεί, μην την επιστρέψεις)
OUTPUT: 1501 rows
*/
SELECT m.title as Title, MIN(r.rating) as MinRating
FROM movie m
INNER JOIN ratings r ON m.id = r.movie_id
GROUP BY m.title

-- 11
/*
Βρες τους πρώτους 5 τίτλους των ταινιών, ως προς την μέση βαθμολογία τους, οι οποίες ανήκουν στο είδος Crime, με έτος παραγωγής μεταξύ του 1970 και του 2020.
OUTPUT: 5 rows
*/
SELECT TOP 5 m.title as Title, AVG(r.rating) as AvgRating, m.release_date as Release_Date
FROM movie m
INNER JOIN hasGenre hG
ON m.id = hG.movie_id
INNER JOIN genre g
ON hG.genre_id = g.id
INNER JOIN ratings r
ON m.id = r.movie_id
WHERE g.name = 'Crime' AND YEAR(m.release_date) BETWEEN 1970 AND 2020
GROUP BY m.title, m.release_date
ORDER BY AvgRating DESC

-- 12
/*
Βρες πόσες ταινίες έχει σκηνοθετήσει κάθε μέλος του movie crew, οι οποίες έχουν popularity μεγαλύτερο
ή ίσο του 5000000 και ταξινόμησέ τες σε φθίνουσα σειρά ως προς τον πλήθος των ταινιών.
OUTPUT: 1680 rows
*/
SELECT DISTINCT c.name as Name, COUNT(m.id) as Number_of_Movies
FROM movie_crew c
INNER JOIN movie m
ON c.movie_id = m.id
WHERE c.job = 'Director' AND m.popularity >= 5000000
GROUP BY c.name
ORDER BY Number_of_Movies DESC 

