create table Costumer(costumerNr bigint not null primary key, firstName varchar(45) not null, lastName varchar(45) not null, birthday varchar(45) not null, address varchar(45) not null);
create table Account(accountNr bigint not null primary key, costumerNr bigint not null, balance double default 0 not null, creditSum double default 0 not null, creditRating double default 1000 not null, creditStatus varchar(300) null, constraint costumerNr foreign key (costumerNr) references Costumer (costumerNr));
create table Credit(CreditNr int not null primary key, sum double not null, costumerNr bigint not null, firstAns smallint default -1 null, secondAns smallint default -1 null, supervisorAns smallint default -1 null, ceoAns smallint default -1 null, start varchar(100) not null, end varchar(100) not null, percentRange double default 4.3 null, constraint creditList_costumerNr_costumerNr_fk foreign key (costumerNr) references Costumer (costumerNr));