-- user
DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
	id integer PRIMARY KEY AUTOINCREMENT,
	username varchar(40) NOT NULL UNIQUE,
	password varchar(40) NOT NULL,
	enabled integer DEFAULT 1
);
