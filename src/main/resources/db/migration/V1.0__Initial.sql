create table person.person (
        person_uuid uuid not null,
        address varchar(255),
        dob date,
        document_expiry_date date,
        document_issuing_country varchar(255),
        document_no varchar(255),
        document_type varchar(255),
        family_name varchar(255),
        gender varchar(255),
        given_name varchar(255),
        nationality varchar(255),
        place varchar(255),
        user_uuid uuid not null,
        primary key (person_uuid)
    )