DROP TABLE IF EXISTS reservations;
CREATE TABLE reservations (id UUID NOT NULL, "from" DATE, "to" DATE, room_id UUID);
