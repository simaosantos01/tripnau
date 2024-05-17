drop table if exists event;

drop table if exists booking;

drop table if exists image;

drop table if exists payment;

drop table if exists price_night_interval;

drop table if exists review;

drop table if exists rental_property;

drop table if exists state;

CREATE TABLE IF NOT EXISTS rental_property
(
    id                   varchar(255) NOT NULL
    PRIMARY KEY,
    price_night_default  float        NOT NULL,
    property_owner_id    varchar(255) NOT NULL,
    property_name        varchar(255) NOT NULL,
    lat                  double       NOT NULL,
    lon                  double       NOT NULL,
    max_guests           int          NOT NULL,
    num_bedrooms         int          NOT NULL,
    num_bathrooms        int          NOT NULL,
    property_description varchar(255) NOT NULL,
    is_active            tinyint(1)   NOT NULL
    );

CREATE TABLE IF NOT EXISTS price_night_interval
(
    id               varchar(255) NOT NULL
    PRIMARY KEY,
    rentalPropertyId varchar(255) NOT NULL,
    price            float        NOT NULL,
    `from`           datetime     NOT NULL,
    `to`             datetime     NOT NULL,
    CONSTRAINT PriceNightInterval_RentalProperty_id_fk
    FOREIGN KEY (rentalPropertyId) REFERENCES rental_property (id)
    );

CREATE TABLE IF NOT EXISTS state
(
    id    varchar(255) NOT NULL
    PRIMARY KEY,
    value varchar(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS user
(
    id        varchar(255)         NOT NULL
    PRIMARY KEY,
    name      varchar(255)         NOT NULL,
    email     varchar(255)         NOT NULL,
    password  varchar(255)         NOT NULL,
    role      varchar(255)         NOT NULL,
    is_banned tinyint(1) DEFAULT 0 NOT NULL
    );

CREATE TABLE IF NOT EXISTS booking
(
    id         varchar(255)                       NOT NULL
    PRIMARY KEY,
    accountId  varchar(255)                       NOT NULL,
    propertyId varchar(255)                       NOT NULL,
    `from`     datetime                           NOT NULL,
    `to`       datetime                           NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT Booking_User_id_fk
    FOREIGN KEY (accountId) REFERENCES user (id),
    CONSTRAINT booking_property_id
    FOREIGN KEY (propertyId) REFERENCES rental_property (id)
    );

CREATE TABLE IF NOT EXISTS event
(
    id        varchar(255)             NOT NULL
    PRIMARY KEY,
    bookingId varchar(255)             NOT NULL,
    dateTime  datetime DEFAULT (NOW()) NOT NULL,
    stateId   varchar(255)             NOT NULL,
    CONSTRAINT Event_Booking_id_fk
    FOREIGN KEY (bookingId) REFERENCES booking (id)
    );

CREATE TABLE IF NOT EXISTS payment
(
    id               varchar(255)                       NOT NULL
    PRIMARY KEY,
    bookingId        varchar(255)                       NOT NULL,
    total            float                              NOT NULL,
    creditCardNumber varchar(255)                       NOT NULL,
    cvc              varchar(255)                       NOT NULL,
    expirationDate   datetime                           NOT NULL,
    email            varchar(255)                       NOT NULL,
    nameOnCard       varchar(255)                       NOT NULL,
    createdAt        datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT payment_booking_fk
    FOREIGN KEY (bookingId) REFERENCES booking (id)
    );

CREATE TABLE IF NOT EXISTS review
(
    id        varchar(255)         NOT NULL
    PRIMARY KEY,
    userId    varchar(255)         NOT NULL,
    bookingId varchar(255)         NOT NULL,
    text      varchar(255)         NOT NULL,
    stars     int                  NOT NULL,
    isBanned  tinyint(1) DEFAULT 0 NOT NULL,
    CONSTRAINT Review_BookingId_id_fk
    FOREIGN KEY (bookingId) REFERENCES booking (id),
    CONSTRAINT Review_User_id_fk
    FOREIGN KEY (userId) REFERENCES user (id),
    CONSTRAINT stars
    CHECK ((`stars` < 6) AND (`stars` > -(1)))
    );

CREATE TABLE IF NOT EXISTS image
(
    id        varchar(255) NOT NULL
    PRIMARY KEY,
    reviewId  varchar(255) NOT NULL,
    reference varchar(255) NOT NULL,
    CONSTRAINT Image_Review_id_fk
    FOREIGN KEY (reviewId) REFERENCES review (id)
    );
