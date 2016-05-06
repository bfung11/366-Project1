INSERT INTO Shifts
VALUES ('7:30 Shift', '07:30', '18:30'),
       ('8:30 Shift', '08:30', '19:30'),
       ('9:30 Shift', '09:30', '20:30'),
       ('Surgery', '07:30', '18:30'),
       ('Overnight Shift', '20:00', '08:30'),
       ('Sunday Shift', '08:00', '20:00');

INSERT INTO Login
VALUES ('first@gmail.com', 'd1', 'password'),
       ('second@gmail.com', 'd2', 'password'),
       ('third@gmail.com', 'd3', 'password'),
       ('fourth@gmail.com', 'd4', 'password'),
       ('fifth@gmail.com', 'd5', 'password'),
       ('sixth@gmail.com', 'd6', 'password'),
       ('seventh@gmail.com', 'd7', 'password'),
       ('eighth@gmail.com', 'd8', 'password'),
       ('ninth@gmail.com', 'd9', 'password'),
       ('tenth@gmail.com', 'd10', 'password'),
       ('first@yahoo.com', 't1', 'password'),
       ('second@yahoo.com', 't2', 'password'),
       ('third@yahoo.com', 't3', 'password'),
       ('fourth@yahoo.com', 't4', 'password'),
       ('fifth@yahoo.com', 't5', 'password'),
       ('sixth@yahoo.com', 't6', 'password'),
       ('seventh@yahoo.com', 't7', 'password'),
       ('eighth@yahoo.com', 't8', 'password'),
       ('ninth@yahoo.com', 't9', 'password'),
       ('tenth@yahoo.com', 't10', 'password'),
       ('admin@hotmail.com', 'admin', 'admin');


INSERT INTO Doctors (email, lastname, phonenumber)
VALUES ('first@gmail.com', 'First', '(111) 111-1111'),
       ('second@gmail.com', 'Second', '(111) 111-1111'),
       ('third@gmail.com', 'Third', '(111) 111-1111'),
       ('fourth@gmail.com', 'Fourth', '(111) 111-1111'),
       ('fifth@gmail.com', 'Fifth', '(111) 111-1111'),
       ('sixth@gmail.com', 'Sixth', '(111) 111-1111'),
       ('seventh@gmail.com', 'Seventh', '(111) 111-1111'),
       ('eighth@gmail.com', 'Eighth', '(111) 111-1111'),
       ('ninth@gmail.com', 'Ninth', '(111) 111-1111');

INSERT INTO Technicians (email, lastname, phonenumber)
VALUES ('first@yahoo.com', 'First', '(111) 111-1111'),
       ('second@yahoo.com', 'Second', '(111) 111-1111'),
       ('third@yahoo.com', 'Third', '(111) 111-1111'),
       ('fourth@yahoo.com', 'Fourth', '(111) 111-1111'),
       ('fifth@yahoo.com', 'Fifth', '(111) 111-1111'),
       ('sixth@yahoo.com', 'Sixth', '(111) 111-1111'),
       ('seventh@yahoo.com', 'Seventh', '(111) 111-1111'),
       ('eighth@yahoo.com', 'Eighth', '(111) 111-1111'),
       ('ninth@yahoo.com', 'Ninth', '(111) 111-1111');

INSERT INTO Administrators (email, lastname, phonenumber) 
VALUES ('admin@hotmail.com', 'last', '(111) 111-1111');
