create database AppDev;

create table Workers
(
    workerNr  int         not null primary key,
    status    varchar(45) not null,
    salary    double      not null,
    date      varchar(45) not null,
    sex       tinyint(1)  not null,
    firstName varchar(45) not null,
    lastName  varchar(45) not null,
    birthday  varchar(45) not null,
    address   varchar(45) not null
);
create table creditHistory
(
    `index`    int             not null primary key,
    creditInfo multilinestring not null,
    constraint creditHistory_index_uindex
    unique (`index`)
);

create table costumerData
(
    costumerNr bigint   not null primary key,
    `index`    int      null,
    firstName  char(45) not null,
    lastName   char(45) not null,
    birthday   char(45) not null,
    address    char(45) not null,
    constraint `index`
        foreign key (`index`) references creditHistory (`index`)
);
create table Account
(
    accountNr    bigint           not null primary key,
    costumerNr   bigint           not null,
    balance      double default 0 not null,
    bankCode     varchar(45)      not null,
    creditRating double default 5 not null,
    constraint costumerNr
        foreign key (costumerNr) references costumerData (costumerNr)
);
create table Bank
(
    accountNr       bigint      not null,
    blz             varchar(45) not null primary key,
    workerNr        int         null,
    costumerNr      bigint      not null,
    Bank_costumerNr bigint      not null,
    constraint Bank_costumerNr
        foreign key (Bank_costumerNr) references costumerData (costumerNr),
    constraint accountNr
        foreign key (accountNr) references Account (accountNr),
    constraint workerNr
        foreign key (workerNr) references Workers (workerNr)
);
create table creditList
(
    CreditNr    int         not null primary key,
    workerNr_1  int         not null,
    workerNr_2  int         not null,
    ceo         int         null,
    sum         double      not null,
    costumerNr  bigint      not null,
    time        bigint      not null,
    date        varchar(45) not null,
    status_1    tinyint(1)  null,
    status_2    tinyint(1)  null,
    ceo_status  tinyint(1)  null,
    percentRate double      null
);
create table creditWorkers
(
    credits  int default 0 not null,
    workerNr int not null primary key
);
create table freeId
(
    accountNr  mediumtext not null,
    costumerNr mediumtext null,
    status     tinyint(1) not null
);