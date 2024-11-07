
DELETE FROM restaurants WHERE id = 5;
UPDATE restaurants
SET approval_status = "APPROVED"
WHERE id = 5;

UPDATE user
SET business_number = "123412372024"
WHERE id = 11;

DROP TABLE IF EXISTS orders;

