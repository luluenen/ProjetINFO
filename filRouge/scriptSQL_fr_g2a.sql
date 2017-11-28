DROP TABLE member;
DROP TABLE team;
DROP TABLE athlete;
DROP TABLE participation;
DROP TABLE podium_bet;
DROP TABLE winner_bet;
DROP TABLE draw_bet;
DROP TABLE bet;
DROP TABLE competitor;
DROP TABLE subscriber;
DROP TABLE competition;


CREATE TABLE subscriber
   (username VARCHAR(255) NOT NULL,   
    lastname VARCHAR(255),
    firstname VARCHAR(255),
    birthdate VARCHAR(255),
    password VARCHAR(255),
    tokens NUMBER(*,0),
    CONSTRAINT subscriber_pk PRIMARY KEY (username)
);
 
CREATE TABLE competition
(
    name VARCHAR(255) NOT NULL,
    competition_date VARCHAR(255) NOT NULL,
    is_sold INT DEFAULT 0 CHECK (is_sold in (0,1)),
    CONSTRAINT competition_pk PRIMARY KEY (name)
);

CREATE TABLE competitor
(
    id_competitor INT PRIMARY KEY NOT NULL
);

CREATE TABLE team
(
    id_team INT PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT fk_team FOREIGN KEY (id_team) REFERENCES competitor(id_competitor) 
);

CREATE TABLE athlete
(
    id_athlete INT NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    birthdate VARCHAR(255),
    CONSTRAINT fk_athlete FOREIGN KEY (id_athlete) REFERENCES competitor(id_competitor),
    CONSTRAINT athlete_pk PRIMARY KEY (id_athlete)
);

CREATE TABLE bet
(
    id_bet NUMBER(*,0),
    gambler VARCHAR(255) NOT NULL,
    competition VARCHAR(255) NOT NULL,
    bet_date VARCHAR(255) NOT NULL,
    tokens_number NUMBER(*,0) NOT NULL,
    CONSTRAINT bet_pk PRIMARY KEY (id_bet),
    CONSTRAINT bet_fk_ref_subscriber FOREIGN KEY (gambler) REFERENCES subscriber(username),
    CONSTRAINT bet_fk_ref_competition FOREIGN KEY (competition) REFERENCES competition(name)
);
 
CREATE TABLE podium_bet
(
    id_bet INT NOT NULL,
     first INT NOT NULL,
     second INT NOT NULL,
    third INT NOT NULL,
    CONSTRAINT podium_bet_pk PRIMARY KEY (id_bet),
    CONSTRAINT podium_id_bet_fk_ref_bet FOREIGN KEY (id_bet) REFERENCES bet(id_bet),
    CONSTRAINT pdm_bet_first_fk_ref_cmptr FOREIGN KEY (first) REFERENCES competitor (id_competitor),
    CONSTRAINT pdm_bet_second_fk_ref_cmptr FOREIGN KEY (second) REFERENCES competitor (id_competitor),
    CONSTRAINT pdm_bet_third_fk_ref_cmptr FOREIGN KEY (third) REFERENCES competitor (id_competitor)
);

CREATE TABLE winner_bet
(
  id_bet INT NOT NULL,
  winner INT NOT NULL,
  CONSTRAINT winner_bet_pk PRIMARY KEY (id_bet),
  CONSTRAINT winner_id_bet_ref_bet FOREIGN KEY (id_bet) REFERENCES bet(id_bet),
  CONSTRAINT winner_bet_winner_ref_cmptr FOREIGN KEY (winner) REFERENCES competitor (id_competitor)
);

CREATE TABLE draw_bet
(
  id_bet INT NOT NULL,
  competitor1 INT NOT NULL,
  competitor2 INT NOT NULL,
  CONSTRAINT draw_bet_pk PRIMARY KEY (id_bet),
  CONSTRAINT draw_id_bet_ref_bet FOREIGN KEY (id_bet) REFERENCES bet(id_bet),
  CONSTRAINT draw_bet_cmptr_1_ref_cmptr FOREIGN KEY (competitor1) REFERENCES competitor(id_competitor),
  CONSTRAINT draw_bet_cmptr_2_ref_cmptr FOREIGN KEY (competitor2) REFERENCES competitor(id_competitor)
);

CREATE TABLE member
(   athlete INT NOT NULL,   
    team INT NOT NULL,
    CONSTRAINT fk1_member FOREIGN KEY (athlete) REFERENCES athlete(id_athlete),
    CONSTRAINT fk2_member FOREIGN KEY (team) REFERENCES team(id_team)
);

CREATE TABLE participation
(
    competitor int NOT NULL,
    competition VARCHAR(255) NOT NULL,
    ranking INT check (ranking>0),
    PRIMARY KEY (competitor, competition),
    CONSTRAINT fk_nom_competition FOREIGN KEY (competition) REFERENCES competition(name),
    CONSTRAINT fk_id_competitor FOREIGN KEY (competitor) REFERENCES competitor(id_competitor)
);



