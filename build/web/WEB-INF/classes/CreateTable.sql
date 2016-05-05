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
   PRIMARY KEY (technicianID, date, shift)
);

CREATE TABLE TechnicianSickDays (
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL
   PRIMARY KEY (id, date)
);

<<<<<<< Updated upstream
CREATE TABLE TechnicianVacationDays (
   id INTEGER REFERENCES Technicians(id),
   date DATE NOT NULL
   PRIMARY KEY (id, date)
);

=======
>>>>>>> Stashed changes
CREATE TABLE Administrators (
   id SERIAL PRIMARY KEY,
   email TEXT UNIQUE NOT NULL,
   firstname TEXT,
   lastname TEXt NOT NULL,
   phonenumber TEXT NOT NULL
);

/* Copy pasta this to drop tables en masse
DROP TABLE Administrators;
<<<<<<< Updated upstream
DROP TABLE TechnicianSickDays;
DROP TABLE TechnicianVacationDays;
=======
DROP TABLE TechnicianTimeOff;
>>>>>>> Stashed changes
DROP TABLE TechnicianPreferredShifts;
DROP TABLE TechnicianShifts;
DROP TABLE Technicians;
DROP TABLE DoctorSickDays;
DROP TABLE DoctorVacationDays;
DROP TABLE DoctorPreferredShifts;
DROP TABLE DoctorShifts;
DROP TABLE Doctors;
DROP TABLE Login;
DROP TABLE Shifts;
*/