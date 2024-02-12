CREATE TABLE rooms
(
    id     bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    number varchar(255) NOT NULL,
    status varchar(255) NOT NULL
);

CREATE TABLE guests
(
    id      bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name    varchar(255) NOT NULL,
    surname varchar(255) NOT NULL
);

CREATE TABLE bookings
(
    id             bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    room_id        bigint       NOT NULL,
    guest_id       bigint       NOT NULL,
    status         varchar(255) NOT NULL,
    check_in_time  timestamp    NOT NULL,
    check_out_time timestamp
);

INSERT INTO rooms(number, status)
VALUES ('1', 'AVAILABLE'),
       ('2', 'AVAILABLE'),
       ('3', 'AVAILABLE'),
       ('4', 'AVAILABLE'),
       ('5', 'AVAILABLE');