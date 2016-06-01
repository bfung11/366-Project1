--
SELECT *
FROM authentications A, staff S
WHERE A.user_type = '<type>' AND
      A.username = S.username

-- createDoctor(), createTechnician()
INSERT INTO authentications
VALUES ('<username>', '<password>', '<user_type');

INSERT INTO staff
VALUES ('<username>', '<email>', '<first_name>', '<last_name>', '<phone_number>')

--
