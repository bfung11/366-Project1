DROP TABLE IF EXISTS vacation_days;
DROP TABLE IF EXISTS preferred_shifts;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS shifts;
DROP TABLE IF EXISTS authentications;

CREATE TABLE authentications (
   PRIMARY KEY (username),
   username TEXT,
   password TEXT NOT NULL,
   user_type TEXT NOT NULL
);

CREATE TABLE shifts (
   shift_name TEXT PRIMARY KEY,
   from_time TIME NOT NULL,
   to_time TIME NOT NULL
);

CREATE TABLE staff (
   PRIMARY KEY (username),
   username      TEXT,
   first_name    TEXT NOT NULL,
   last_name     TEXT NOT NULL,
   phone_number  TEXT NOT NULL,
   email         TEXT UNIQUE NOT NULL,
   vacation_days INTEGER DEFAULT 8,
   sick_days     INTEGER DEFAULT 4,
                 FOREIGN KEY (username) REFERENCES authentications(username),
                 FOREIGN KEY (email) REFERENCES authentications (email)
);

CREATE TABLE schedule (
   PRIMARY KEY (id),
   id         SERIAL,
   username   TEXT,
   shift_date DATE NOT NULL,
   shift_name TEXT NOT NULL,
              FOREIGN KEY username REFERENCES authentications(username),
              FOREIGN KEY shift_name REFERENCES shifts(shift_name)
);

CREATE TABLE preferred_shifts (
   PRIMARY KEY (id),
   id         SERIAL,
   username   TEXT,
   shift_date DATE NOT NULL,
   shift_name TEXT NOT NULL,
              FOREIGN KEY username REFERENCES authentications(username),
              FOREIGN KEY shift_name REFERENCES shifts(shift_name)
);

CREATE TABLE vacation_days (
   PRIMARY KEY (id),
   id       SERIAL,
   username TEXT,
   date_off DATE NOT NULL,
            FOREIGN KEY username REFERENCES authentications(username)
);

INSERT INTO authentications
VALUES ('admin', 'admin', 'admin'),
       ('d1', 'd1'. 'doctor'),
       ('d2', 'd2'. 'doctor'),
       ('d3', 'd3'. 'doctor'),
       ('d4', 'd4'. 'doctor'),
       ('d5', 'd5'. 'doctor'),
       ('d6', 'd6'. 'doctor'),
       ('d7', 'd7'. 'doctor'),
       ('d8', 'd8'. 'doctor'),
       ('d9', 'd9'. 'doctor'),
       ('d10', 'd10'. 'doctor'),
       ('t1', 't1'. 'technician'),
       ('t2', 't2'. 'technician'),
       ('t3', 't3'. 'technician'),
       ('t4', 't4'. 'technician'),
       ('t5', 't5'. 'technician'),
       ('t6', 't6'. 'technician'),
       ('t7', 't7'. 'technician'),
       ('t8', 't8'. 'technician'),
       ('t9', 't9'. 'technician'),
       ('t10', 't10'. 'technician');

INSERT INTO shifts
VALUES ('Early Morning', '07:30', '18:30'),
       ('Morning', '08:30', '19:30'),
       ('Late Morning', '09:30', '20:30'),
       ('Surgery', '07:30', '18:30'),
       ('Overnight', '20:00', '08:30'),
       ('Sunday', '08:00', '20:00');


INSERT INTO staff
VALUES ('d1', 'doctor1@gmail.com', 'first', 'doctor', '12345678890', 'doctor'),
       ('d2', 'doctor2@gmail.com', 'second', 'doctor', '12345678890', 'doctor'),
       ('d3', 'doctor3@gmail.com', 'third', 'doctor', '12345678890', 'doctor'),
       ('d4', 'doctor4@gmail.com', 'fourth', 'doctor', '12345678890', 'doctor'),
       ('d5', 'doctor5@gmail.com', 'fifth', 'doctor', '12345678890', 'doctor'),
       ('d6', 'doctor6@gmail.com', 'sixth', 'doctor', '12345678890', 'doctor'),
       ('d7', 'doctor7@gmail.com', 'seventh', 'doctor', '12345678890', 'doctor'),
       ('d8', 'doctor8@gmail.com', 'eigth', 'doctor', '12345678890', 'doctor'),
       ('d9', 'doctor9@gmail.com', 'ninth', 'doctor', '12345678890', 'doctor'),
       ('d10', 'doctor10@gmail.com', 'tenth', 'doctor', '12345678890', 'doctor'),

       ('t1', 'technician1@gmail.com', 'first', 'technician', '12345678890', 'technician'),
       ('t2', 'technician2@gmail.com', 'second', 'technician', '12345678890', 'technician'),
       ('t3', 'technician3@gmail.com', 'third', 'technician', '12345678890', 'technician'),
       ('t4', 'technician4@gmail.com', 'fourth', 'technician', '12345678890', 'technician'),
       ('t5', 'technician5@gmail.com', 'fifth', 'technician', '12345678890', 'technician'),
       ('t6', 'technician6@gmail.com', 'sixth', 'technician', '12345678890', 'technician'),
       ('t7', 'technician7@gmail.com', 'seventh', 'technician', '12345678890', 'technician'),
       ('t8', 'technician8@gmail.com', 'eigth', 'technician', '12345678890', 'technician'),
       ('t9', 'technician9@gmail.com', 'ninth', 'technician', '12345678890', 'technician'),
       ('t10', 'technician10@gmail.com', 'tenth', 'technician', '12345678890', 'technician');

INSERT INTO schedule 
VALUES ('d4', '2016-01-20', 'Sunday'),
       ('d1', '2016-01-20', 'Overnight'),

       ('d8', '2016-01-21', 'Early Morning'),
       ('d5', '2016-01-21', 'Morning'),
       ('d6', '2016-01-21', 'Late Morning'),
       ('d7', '2016-01-21', 'Surgery'),
       ('d2', '2016-01-21', 'Overnight'),

       ('d5', '2016-01-22', 'Early Morning'),
       ('d9', '2016-01-22', 'Early Morning'),
       ('d6', '2016-01-22', 'Morning'),
       ('d7', '2016-01-22', 'Late Morning'),
       ('d1', '2016-01-22', 'Surgery'),
       ('d3', '2016-01-22', 'Overnight'),

       ('d6', '2016-01-23', 'Early Morning'),
       ('d9', '2016-01-23', 'Early Morning'),
       ('d7', '2016-01-23', 'Morning'),
       ('d1', '2016-01-23', 'Late Morning'),
       ('d2', '2016-01-23', 'Surgery'),
       ('d4', '2016-01-23', 'Overnight'),

       ('d8', '2016-01-24', 'Early Morning'),
       ('d1', '2016-01-24', 'Morning'),
       ('d2', '2016-01-24', 'Late Morning'),
       ('d3', '2016-01-24', 'Surgery'),
       ('d5', '2016-01-24', 'Overnight'),

       ('d9', '2016-01-25', 'Early Morning'),
       ('d2', '2016-01-25', 'Morning'),
       ('d3', '2016-01-25', 'Late Morning'),
       ('d8', '2016-01-25', 'Late Morning'),
       ('d4', '2016-01-25', 'Surgery'),
       ('d6', '2016-01-25', 'Overnight'),

       ('d9', '2016-01-26', 'Early Morning'),
       ('d3', '2016-01-26', 'Morning'),
       ('d4', '2016-01-26', 'Late Morning'),
       ('d8', '2016-01-26', 'Late Morning'),
       ('d5', '2016-01-26', 'Surgery'),
       ('d7', '2016-01-26', 'Overnight');




