UPDATE person.person SET given_name = LEFT(given_name, 35);
ALTER TABLE person.person ALTER COLUMN given_name type VARCHAR(35);

UPDATE person.person SET family_name = LEFT(family_name, 35);
ALTER TABLE person.person ALTER COLUMN family_name type VARCHAR(35);

UPDATE person.person SET address = LEFT(address, 35);
ALTER TABLE person.person ALTER COLUMN address type VARCHAR(35);

UPDATE person.person SET place = LEFT(place, 35);
ALTER TABLE person.person ALTER COLUMN place type VARCHAR(35);

UPDATE person.person SET nationality = LEFT(nationality, 3);
ALTER TABLE person.person ALTER COLUMN nationality type VARCHAR(3);

UPDATE person.person SET document_no = LEFT(document_no, 44);
ALTER TABLE person.person ALTER COLUMN document_no type VARCHAR(44);

UPDATE person.person SET document_issuing_country = LEFT(document_issuing_country, 3);
ALTER TABLE person.person ALTER COLUMN document_issuing_country type VARCHAR(3);

