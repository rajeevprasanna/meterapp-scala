

-- drop page table and re-create
drop table if exists page;
create table page (
	id int not null auto_increment,
	name char(25) not null,
	primary key(id)
);


-- drop grouping table and re-create
drop table if exists grouping;
create table grouping (
	id int not null auto_increment,
	name char(25) not null,
	primary key(id)
);


-- drop meter table and re-create
drop table if exists meter;
create table meter (
	id int not null auto_increment,
	name char(25) not null,
	primary key(id)
);


-- drop the grouping_meter table and re-create
drop table if exists grouping_meter;
create table grouping_meter (
	id int not null auto_increment,
	groupingid int not null references grouping(id),
	meterid int not null references meter(id),
	primary key(id)
);


-- drop the page_grouping table and re-create
drop table if exists page_grouping;
create table page_grouping (
	id int not null auto_increment,
	pageid int not null references page(id),
	groupingid int not null references grouping(id),
	primary key(id)
);


-- populate the meters, groupings, and pages
insert into meter (name) values ('Meter 01');
insert into meter (name) values ('Meter 02');
insert into meter (name) values ('Meter 03');
insert into meter (name) values ('Meter 04');
insert into meter (name) values ('Meter 05');
insert into meter (name) values ('Meter 06');
insert into meter (name) values ('Meter 07');
insert into meter (name) values ('Meter 08');
insert into meter (name) values ('Meter 09');
insert into meter (name) values ('Meter 10');

insert into grouping (name) values ('Grouping 01');
insert into grouping (name) values ('Grouping 02');
insert into grouping (name) values ('Grouping 03');
insert into grouping (name) values ('Grouping 04');
insert into grouping (name) values ('Grouping 05');

insert into page (name) values ('Page 01');
insert into page (name) values ('Page 02');
insert into page (name) values ('Page 03');

insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 01'),
	 (select id from meter where name = 'Meter 01'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 01'),
	 (select id from meter where name = 'Meter 02'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 01'),
	 (select id from meter where name = 'Meter 03'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 02'),
	 (select id from meter where name = 'Meter 04'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 02'),
	 (select id from meter where name = 'Meter 05'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 03'),
	 (select id from meter where name = 'Meter 06'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 03'),
	 (select id from meter where name = 'Meter 07'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 03'),
	 (select id from meter where name = 'Meter 08'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 03'),
	 (select id from meter where name = 'Meter 09'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 04'),
	 (select id from meter where name = 'Meter 01'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 04'),
	 (select id from meter where name = 'Meter 10'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 05'),
	 (select id from meter where name = 'Meter 03'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 05'),
	 (select id from meter where name = 'Meter 05'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 05'),
	 (select id from meter where name = 'Meter 07'));
insert into grouping_meter (groupingid, meterid) values 
	((select id from grouping where name = 'Grouping 05'),
	 (select id from meter where name = 'Meter 09'));

insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 01'),
	 (select id from grouping where name = 'Grouping 01'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 01'),
	 (select id from grouping where name = 'Grouping 02'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 01'),
	 (select id from grouping where name = 'Grouping 03'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 02'),
	 (select id from grouping where name = 'Grouping 04'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 02'),
	 (select id from grouping where name = 'Grouping 05'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 03'),
	 (select id from grouping where name = 'Grouping 01'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 03'),
	 (select id from grouping where name = 'Grouping 03'));
insert into page_grouping (pageid, groupingid) values 
	((select id from page where name = 'Page 03'),
	 (select id from grouping where name = 'Grouping 05'));

commit;