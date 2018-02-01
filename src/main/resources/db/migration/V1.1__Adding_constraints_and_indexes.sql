alter table person.person alter column gender type varchar(12);

create index personUserIdx on person.person (user_uuid);
create index personPersonIdx on person.person (person_uuid);
