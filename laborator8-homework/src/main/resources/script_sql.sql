
CREATE SEQUENCE continents_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE continents (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL UNIQUE
);

CREATE OR REPLACE TRIGGER continents_trigger
BEFORE INSERT ON continents
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT continents_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE SEQUENCE countries_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE countries (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL UNIQUE,
    continent_id NUMBER NOT NULL,
    CONSTRAINT fk_continent FOREIGN KEY (continent_id) REFERENCES continents(id)
);

CREATE OR REPLACE TRIGGER countries_trigger
BEFORE INSERT ON countries
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT countries_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

SELECT table_name FROM user_tables;

INSERT INTO continents (name) VALUES ('Europe'); --id=1
INSERT INTO continents (name) VALUES ('Asia'); --id=2
INSERT INTO continents (name) VALUES ('Africa'); --id=3
INSERT INTO continents (name) VALUES ('North America'); --id=4
INSERT INTO continents (name) VALUES ('South America'); --id=5
INSERT INTO continents (name) VALUES ('Australia'); --id=6
INSERT INTO continents (name) VALUES ('Antarctica'); --id=7
COMMIT;

INSERT INTO countries (name, continent_id) VALUES ('Romania', 1);
INSERT INTO countries (name, continent_id) VALUES ('Germany', 1);
INSERT INTO countries (name, continent_id) VALUES ('France', 1);
INSERT INTO countries (name, continent_id) VALUES ('China', 2);
INSERT INTO countries (name, continent_id) VALUES ('India', 2);
INSERT INTO countries (name, continent_id) VALUES ('Japan', 2);
INSERT INTO countries (name, continent_id) VALUES ('South Korea', 2);
INSERT INTO countries (name, continent_id) VALUES ('Egypt', 3);
INSERT INTO countries (name, continent_id) VALUES ('South Africa', 3);
INSERT INTO countries (name, continent_id) VALUES ('Nigeria', 3);
INSERT INTO countries (name, continent_id) VALUES ('United States', 4);
INSERT INTO countries (name, continent_id) VALUES ('Canada', 4);
INSERT INTO countries (name, continent_id) VALUES ('Mexico', 4);
INSERT INTO countries (name, continent_id) VALUES ('Brazil', 5);
INSERT INTO countries (name, continent_id) VALUES ('Argentina', 5);
INSERT INTO countries (name, continent_id) VALUES ('Colombia', 5);
INSERT INTO countries (name, continent_id) VALUES ('Australia', 6);
INSERT INTO countries (name, continent_id) VALUES ('New Zealand', 6);
COMMIT;