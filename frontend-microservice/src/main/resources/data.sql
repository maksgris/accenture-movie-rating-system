INSERT INTO defaultdb.user_type (id,type) VALUES
(1,"admin"),
(2,"defaultUser"),
(3,"beginnerUser"),
(4,"advancedUser"),
(5,"gigaUser");


INSERT INTO defaultdb.users (name,surname,email,user_type_id) VALUES
('Alexis','Merceris',"testEmail@cool.com",1),
('Gordon','Freedomman',"hf3@never.com",2),
('Big','Lebowski',"money@ishere.com",3),
('Adam','Sander',"click@magic.com",4),
('Britain','Pears',"notoxicity@again.oops",5),
('Pressiks','Tojason',"find@shawn.com",5);

INSERT INTO defaultdb.movie_type (type) VALUES
("horror"),("fantasy"),("sci-fi"),("action"),("comedy");

INSERT INTO defaultdb.movie (title,description,movie_type) VALUES
("CyborgNinja", "In a post-apocalyptic world where technology and human augmentation have gone too far, a group of rogue scientists creates the ultimate weapon: a cyborg ninja. ", 3),
("Lord Of The Rings", "In a fantastical world of Middle Earth, a young hobbit named Frodo must destroy the evil ring of power before it falls into the hands of the dark lord Sauron.", 2),
("Another Adam Sandler Movie",  "the comedian stars as a down-on-his-luck man who must navigate a series of absurd situations and outrageous characters to win back the love of his life.", 5),
("Game of sinke", " a group of skilled gamers compete in a virtual reality battle royale tournament for a chance to win a massive cash prize. ", 3),
("House of sinke", " is a spine-chilling horror film about a group of friends who find themselves trapped in a haunted house, where they must fight to survive the night.", 2),
("Americans", "A gripping drama that follows the lives of diverse Americans as they navigate through love, loss, and the pursuit of the American dream.", 5),
("Creator", "A brilliant scientist creates the ultimate artificial intelligence, but must confront the consequences of playing god when his creation begins to question its own existence.", 3),
("Disturbed", " A troubled young woman spirals into madness as she is haunted by her past and the disturbing secrets of her family.", 2),
("down with the sickness", "A group of survivors must band together to fight against a deadly virus that has infected the world and turned its victims into mindless, bloodthirsty monsters.", 5),
("LP tribute", " A heartfelt tribute to the legendary rock band Linkin Park and the legacy of its late frontman Chester Bennington.", 3),
("Fantasy movie 2", "An epic adventure through a magical world filled with mythical creatures and ancient legends, as a group of heroes embark on a quest to save their kingdom from an evil sorcerer.", 2),
("Fantasy movie 3", "The final chapter in an epic trilogy that pits our heroes against their greatest enemy yet, as they fight to restore balance to a world on the brink of destruction.", 5);

INSERT INTO defaultdb.review (review_date,score,text_review,movie_id,user_id) VALUES
('2010-03-21', 5 , 'Still better than Robocop',3, 1),
('2010-03-21', 10 , "N3ver fails to impress, another masterpiece",3, 4),
('2010-03-21', 1 , "Boooooring",2, 4),
('2010-03-21', 10 , "Best piece of cinematography",2, 1),
('2010-03-21', 10 , "...", 1, 2),
('2010-03-21', 1 , "...",3, 2),
('2010-03-21', 10 , "DUUUUDE , Best piece of cinematography ",2, 3),
('2010-03-21', 10 , "B3st p13c3 0f kin3m@tOgr@phy EVERRRR",2, 5);

INSERT INTO defaultdb.user_likes (review_id, user_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),
(2,1),(2,3),(2,5),(2,6),
(3,2),(3,4),(3,6),
(4,1),
(5,1),(5,6),
(6,4),(6,3),
(8,5);

INSERT INTO defaultdb.movie_likes (movie_id, user_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),
(2,1),(2,3),(2,5),(2,6),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),
(4,1),(4,3),(4,5),(4,6),
(5,5),(5,2),(5,3),(5,4),(5,5),(5,6),
(6,1),
(7,1),
(8,2),
(9,3),
(10,1),(10,3);