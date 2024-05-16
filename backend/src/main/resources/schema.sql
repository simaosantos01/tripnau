create table payment
(
    id               varchar(255)                       not null
        primary key,
    total            float                              not null,
    creditCardNumber varchar(255)                       not null,
    cvc              varchar(255)                       not null,
    expirationDate   datetime                           not null,
    email            varchar(255)                       not null,
    nameOnCard       varchar(255)                       not null,
    createdAt        datetime default CURRENT_TIMESTAMP not null
);

create table rental_property
(
    id                  varchar(255) not null
        primary key,
    priceNightDefault   float        not null,
    propertyOwnerId     int          not null,
    propertyName        varchar(255) not null,
    lat                 double       not null,
    lon                 double       not null,
    maxGuests           int          not null,
    numOfBedrooms       int          not null,
    numOfBathrooms      int          not null,
    propertyDescription varchar(255) not null,
    constraint maxGuests
        check (`maxGuests` >= 0),
    constraint numOfBathrooms
        check (`numOfBathrooms` >= 0),
    constraint numOfBedrooms
        check (`numOfBedrooms` >= 0),
    constraint priceNightDefault
        check (`priceNightDefault` = round(`maxGuests`, 2))
);

create table price_night_interval
(
    id               varchar(255) not null
        primary key,
    rentalPropertyId varchar(255) not null,
    price            float        not null,
    `from`           datetime     not null,
    `to`             datetime     not null,
    constraint PriceNightInterval_RentalProperty_id_fk
        foreign key (rentalPropertyId) references rental_property (id)
);

create table state
(
    id    varchar(255) not null
        primary key,
    value varchar(255) not null
);

create table user
(
    id        varchar(255)         not null
        primary key,
    name      varchar(255)         not null,
    email     varchar(255)         not null,
    password  varchar(255)         not null,
    role      varchar(255)         not null,
    is_banned tinyint(1) default 0 not null
);

create table booking
(
    id         varchar(255)                       not null
        primary key,
    accountId  varchar(255)                       not null,
    paymentId  varchar(255)                       not null,
    `from`     datetime                           not null,
    `to`       datetime                           not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint Booking_Payment_id_fk
        foreign key (paymentId) references payment (id),
    constraint Booking_User_id_fk
        foreign key (accountId) references user (id)
);

create table event
(
    id        varchar(255)             not null
        primary key,
    bookingId varchar(255)             not null,
    dateTime  datetime default (now()) not null,
    stateId   varchar(255)             not null,
    constraint Event_Booking_id_fk
        foreign key (bookingId) references booking (id)
);

create table review
(
    id               varchar(255)         not null
        primary key,
    userId           varchar(255)         not null,
    rentalPropertyId varchar(255)         not null,
    text             varchar(255)         not null,
    stars            int                  not null,
    isBanned         tinyint(1) default 0 not null,
    constraint Review_RentalProperty_id_fk
        foreign key (rentalPropertyId) references rental_property (id),
    constraint Review_User_id_fk
        foreign key (userId) references user (id),
    constraint stars
        check ((`stars` < 6) and (`stars` > -(1)))
);

create table image
(
    id        varchar(255) not null
        primary key,
    reviewId  varchar(255) not null,
    reference varchar(255) not null,
    constraint Image_Review_id_fk
        foreign key (reviewId) references review (id)
);