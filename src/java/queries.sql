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