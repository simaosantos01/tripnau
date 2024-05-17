drop table if exists event;

drop table if exists booking;

drop table if exists image;

drop table if exists payment;

drop table if exists price_night_interval;

drop table if exists review;

drop table if exists rental_property;

drop table if exists state;

create table rental_property
(
    id                   varchar(255) not null
        primary key,
    price_night_default  float        not null,
    property_owner_id    varchar(255) not null,
    property_name        varchar(255) not null,
    lat                  double       not null,
    lon                  double       not null,
    max_guests           int          not null,
    num_bedrooms         int          not null,
    num_bathrooms        int          not null,
    property_description varchar(255) not null,
    is_active            tinyint(1)   not null
);

create table price_night_interval
(
    id                 varchar(255) not null
        primary key,
    rental_property_id varchar(255) not null,
    price              float        not null,
    from_date          date         not null,
    to_date            date         not null,
    constraint PriceNightInterval_RentalProperty_id_fk
        foreign key (rental_property_id) references rental_property (id)
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
    id          varchar(255)                       not null
        primary key,
    account_id  varchar(255)                       not null,
    property_id varchar(255)                       not null,
    from_date   date                               not null,
    to_date     date                               not null,
    created_at  datetime default CURRENT_TIMESTAMP not null,
    constraint Booking_User_id_fk
        foreign key (account_id) references user (id),
    constraint booking_property_id
        foreign key (property_id) references rental_property (id)
);

create table event
(
    id         varchar(255)             not null
        primary key,
    booking_id varchar(255)             not null,
    date_time  datetime default (now()) not null,
    state_id   varchar(255)             not null,
    constraint Event_Booking_id_fk
        foreign key (booking_id) references booking (id)
);

create table payment
(
    id                 varchar(255)                       not null
        primary key,
    booking_id         varchar(255)                       not null,
    total              float                              not null,
    credit_card_number varchar(255)                       not null,
    cvc                varchar(255)                       not null,
    expiration_date    datetime                           not null,
    email              varchar(255)                       not null,
    name_on_card       varchar(255)                       not null,
    created_at         datetime default CURRENT_TIMESTAMP not null,
    constraint payment_booking_fk
        foreign key (booking_id) references booking (id)
);

create table review
(
    id         varchar(255)         not null
        primary key,
    user_id    varchar(255)         not null,
    booking_id varchar(255)         not null,
    text       varchar(255)         not null,
    stars      int                  not null,
    is_banned  tinyint(1) default 0 not null,
    constraint Review_BookingId_id_fk
        foreign key (booking_id) references booking (id),
    constraint Review_User_id_fk
        foreign key (user_id) references user (id),
    constraint stars
        check ((`stars` < 6) and (`stars` > -(1)))
);

create table image
(
    id        varchar(255) not null
        primary key,
    review_id varchar(255) not null,
    reference varchar(255) not null,
    constraint Image_Review_id_fk
        foreign key (review_id) references review (id)
);

