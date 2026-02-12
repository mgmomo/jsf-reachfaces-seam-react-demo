-- Seed data loaded by Hibernate on schema creation

INSERT INTO location (id, location_name, address, zip_code, state) VALUES (1, 'Main Office', '123 Business Ave', '10001', 'ACTIVE');
INSERT INTO location (id, location_name, address, zip_code, state) VALUES (2, 'Warehouse North', '456 Industrial Blvd', '20002', 'ACTIVE');
INSERT INTO location (id, location_name, address, zip_code, state) VALUES (3, 'Branch South', '789 Commerce St', '30003', 'ACTIVE');
INSERT INTO location (id, location_name, address, zip_code, state) VALUES (4, 'Old Depot', '321 Legacy Rd', '40004', 'NOT_ACTIVE');

INSERT INTO person (id, first_name, last_name, date_of_birth) VALUES (1, 'John', 'Smith', '1985-03-15');
INSERT INTO person (id, first_name, last_name, date_of_birth) VALUES (2, 'Jane', 'Doe', '1990-07-22');
INSERT INTO person (id, first_name, last_name, date_of_birth) VALUES (3, 'Bob', 'Johnson', '1978-11-08');

INSERT INTO person_location (person_id, location_id) VALUES (1, 1);
INSERT INTO person_location (person_id, location_id) VALUES (1, 2);
INSERT INTO person_location (person_id, location_id) VALUES (2, 1);
INSERT INTO person_location (person_id, location_id) VALUES (3, 3);
