DROP TABLE IF EXISTS Administrators;
DROP TABLE IF EXISTS TechnicianSickDays;
DROP TABLE IF EXISTS TechnicianVacationDays;
DROP TABLE IF EXISTS TechnicianPreferredShifts;
DROP TABLE IF EXISTS TechnicianShifts;
DROP TABLE IF EXISTS Technicians;
DROP TABLE IF EXISTS DoctorSickDays;
DROP TABLE IF EXISTS DoctorVacationDays;
DROP TABLE IF EXISTS DoctorPreferredShifts;
DROP TABLE IF EXISTS DoctorShifts;
DROP TABLE IF EXISTS Doctors;
DROP TABLE IF EXISTS Login;
DROP TABLE IF EXISTS Shifts;

CREATE TABLE Shifts (
   name TEXT PRIMARY KEY,
   fromTime TIME NOT NULL,
   toTime TIME NOT NULL
);

CREATE TABLE Login (
   email TEXT PRIMARY KEY,
   username TEXT UNIQUE NOT NULL,
   password TEXT NOT NULL
);

CREATE TABLE Doctors (
   id SERIAL PRIMARY KEY,
   email TEXT REFERENCES Login(email),
   firstname TEXT,
   lastname TEXT NOT NULL,
   phonenumber TEXT NOT NULL,
   vacationDaysLeft INTEGER DEFAULT 8,
   sickDaysLeft INTEGER DEFAULT 4
);

-- DoctorShifts connects a doctor to a shift and a date
-- Can have two doctors per shift
CREATE TABLE DoctorShifts (
   id INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (id, date)
);

CREATE TABLE DoctorPreferredShifts (
   id INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (id, date, shift)
);

CREATE TABLE DoctorSickDays (
   id INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   PRIMARY KEY (id, date)
);

CREATE TABLE DoctorVacationDays (
   id INTEGER REFERENCES Doctors(id),
   date DATE NOT NULL,
   PRIMARY KEY (id, date)
);

CREATE TABLE Technicians (
   id SERIAL PRIMARY KEY,
   email TEXT REFERENCES Login(email),
   firstname TEXT,
   lastname TEXt NOT NULL,
   phonenumber TEXT NOT NULL,
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
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL,
   shift TEXT REFERENCES Shifts(name),
   PRIMARY KEY (id, date, shift)
);

CREATE TABLE TechnicianSickDays (
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL,
   PRIMARY KEY (id, date)
);

CREATE TABLE TechnicianVacationDays (
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL,
   PRIMARY KEY (id, date)
);

CREATE TABLE Administrators (
   id SERIAL PRIMARY KEY,
   email TEXT UNIQUE NOT NULL,
   firstname TEXT,
   lastname TEXt NOT NULL,
   phonenumber TEXT NOT NULL
);