CREATE TABLE Shifts (
   name TEXT PRIMARY KEY,
   fromTime TIME NOT NULL,
   toTime TIME NOT NULL
);

CREATE TABLE Doctors (
   id SERIAL PRIMARY KEY,
   email TEXT UNIQUE NOT NULL,
   username TEXT UNIQUE NOT NULL,
   password TEXT NOT NULL,
   firstname TEXT,
   lastname TEXT NOT NULL,
   phone TEXT NOT NULL,
   vacationDaysLeft INTEGER DEFAULT 8,
   sickDaysLeft INTEGER DEFAULT 4
);

-- DoctorShifts connects a doctor to a shift and a date
CREATE TABLE DoctorShifts (
   id INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (id, date)
);

CREATE TABLE DoctorPreferredShifts (
   doctorID INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (doctorID, date, shift)
);

CREATE TABLE DoctorTimeOff (
   id INTEGER REFERENCES Doctors(id),
   fromDate DATE NOT NULL,
   fromTime TIME NOT NULL,
   toDate DATE NOT NULL,
   toTime TIME NOT NULL,
   timeOffType TEXT NOT NULL,
   PRIMARY KEY (id, fromDate, fromTime, toDate, toTime)
);

CREATE TABLE Technicians (
   id SERIAL PRIMARY KEY,
   email TEXT UNIQUE NOT NULL,
   username TEXT UNIQUE NOT NULL,
   password TEXT NOT NULL,
   firstname TEXT,
   lastname TEXt NOT NULL,
   phone TEXT NOT NULL,
   vacationDaysLeft INTEGER DEFAULT 8,
   sickDaysLeft INTEGER DEFAULT 4
);

-- TechnicianShifts connects a doctor to a shift and a date
CREATE TABLE TechnicianShifts (
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (id, date)
);

CREATE TABLE TechnicianPreferredShifts (
   technicianID INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (technicianID, date, shift)
);

CREATE TABLE TechnicianTimeOff (
   id INTEGER REFERENCES Doctors(id),
   fromDate DATE NOT NULL,
   fromTime TIME NOT NULL,
   toDate DATE NOT NULL,
   toTime TIME NOT NULL,
   timeOffType TEXT NOT NULL,
   PRIMARY KEY (id, fromDate, fromTime, toDate, toTime)
);

CREATE TABLE Admins (
   id SERIAL PRIMARY KEY,
   email TEXT UNIQUE NOT NULL,
   username TEXT UNIQUE NOT NULL,
   password TEXT NOT NULL,
   firstname TEXT,
   lastname TEXt NOT NULL,
   phone TEXT NOT NULL
);

/*
-- Example code for inserting multiple values 
INSERT INTO Days
VALUES('2016-04-12'),
      ('2016-05-12'),
      ('2016-06-12');
*/

/* Copy pasta this to drop tables en masse
DROP TABLE Admins;
DROP TABLE TechnicianTimeOff;
DROP TABLE TechnicianPreferredShifts;
DROP TABLE TechnicianShifts;
DROP TABLE Technicians;
DROP TABLE DoctorTimeOff;
DROP TABLE DoctorPreferredShifts;
DROP TABLE DoctorShifts;
DROP TABLE Doctors;
DROP TABLE Shifts;
*/