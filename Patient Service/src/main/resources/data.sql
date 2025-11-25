-- Ensure the 'patient' table exists
CREATE TABLE IF NOT EXISTS patient
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255)        NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    address         VARCHAR(255)        NOT NULL,
    date_of_birth   DATE                NOT NULL,
    registered_date DATE                NOT NULL
    );

-- Sample Patient Data
INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614170000',
       'Aarav Sharma',
       'aarav.sharma@example.com',
       '12 MG Road, Indiranagar, Bengaluru',
       '1998-04-12',
       '2024-02-10'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '123e4567-e89b-12d3-a456-426614170000');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614170001',
       'Priya Nair',
       'priya.nair@example.com',
       '44 Marine Drive, Kochi, Kerala',
       '1995-11-03',
       '2023-12-15'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '123e4567-e89b-12d3-a456-426614170001');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614170002',
       'Rohan Gupta',
       'rohan.gupta@example.com',
       '221B Park Street, Kolkata',
       '1989-07-29',
       '2024-01-05'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '123e4567-e89b-12d3-a456-426614170002');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614170003',
       'Kavya Reddy',
       'kavya.reddy@example.com',
       'Hitech City, Madhapur, Hyderabad',
       '1997-02-18',
       '2024-03-22'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '123e4567-e89b-12d3-a456-426614170003');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '123e4567-e89b-12d3-a456-426614170004',
       'Vikram Singh',
       'vikram.singh@example.com',
       'Sector 62, Noida, Uttar Pradesh',
       '1984-09-14',
       '2023-11-09'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '123e4567-e89b-12d3-a456-426614170004');


-- More Indian data
INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '223e4567-e89b-12d3-a456-426614170005',
       'Sneha Patil',
       'sneha.patil@example.com',
       'FC Road, Shivaji Nagar, Pune',
       '1993-01-27',
       '2024-04-10'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614170005');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '223e4567-e89b-12d3-a456-426614170006',
       'Arjun Mehta',
       'arjun.mehta@example.com',
       'C G Road, Navrangpura, Ahmedabad',
       '1990-06-08',
       '2023-10-12'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614170006');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '223e4567-e89b-12d3-a456-426614170007',
       'Divya Iyer',
       'divya.iyer@example.com',
       'T Nagar, Chennai',
       '1987-03-16',
       '2024-02-28'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614170007');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '223e4567-e89b-12d3-a456-426614170008',
       'Harsh Vardhan',
       'harsh.vardhan@example.com',
       'Rajajinagar, Bengaluru',
       '1996-12-02',
       '2023-09-05'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614170008');

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
SELECT '223e4567-e89b-12d3-a456-426614170009',
       'Meera Shah',
       'meera.shah@example.com',
       'Altamount Road, Mumbai',
       '1992-08-21',
       '2024-01-18'
    WHERE NOT EXISTS (SELECT 1 FROM patient WHERE id = '223e4567-e89b-12d3-a456-426614170009');
