/*****************
 * Admin Queries *
 *****************/

-- listStaff()
SELECT *
FROM authentications A, staff S
WHERE A.user_type = '<type>' AND
      A.username = S.username

-- addStaff()
INSERT INTO authentications
VALUES ('<username>', '<password>', '<user_type');

INSERT INTO staff
VALUES ('<username>', '<email>', '<first_name>', '<last_name>', '<phone_number>')

-- deleteStaff()
DELETE FROM authentications
WHERE username = 'username';

DELETE FROM staff
WHERE username = 'username';

-- changeStaffPassword()
UPDATE authentications
SET password = 'password'
WHERE username = 'username';


/*****************
 * Staff Queries *
 *****************/
-- viewSchedule()
SELECT *
FROM authentications A, 

-- choosePreferredShift()
INSERT INTO preferred_shifts
VALUES (DEFAULT, 'username', 'date', 'shiftName');

-- chooseTimeOff()
INSERT INTO time_off
VALUES (DEFAULT, 'username', 'date');