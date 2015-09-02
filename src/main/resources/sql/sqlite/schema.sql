-- user
DROP TABLE IF EXISTS "user";
CREATE TABLE "user" (
	id integer PRIMARY KEY AUTOINCREMENT,
	username varchar(40) NOT NULL UNIQUE,
	password varchar(40) NOT NULL,
	enabled integer DEFAULT 1
);

DROP TABLE IF EXISTS "project";
CREATE TABLE "project" (
	id integer PRIMARY KEY AUTOINCREMENT,
	name varchar(25) NOT NULL,
	base_url varchar(128)
);

DROP TABLE IF EXISTS "test_case";
CREATE TABLE "test_case" (
	id integer PRIMARY KEY AUTOINCREMENT,
	project_id integer NOT NULL,
	method integer,
	path varchar(128),
	description varchar(128),
	rank integer DEFAULT 0
);

DROP TABLE IF EXISTS "case_info";
CREATE TABLE "case_info" (
	"id" integer PRIMARY KEY AUTOINCREMENT,
	"case_id" integer NOT NULL,
	"type" integer DEFAULT 0,
	"name" varchar(40),
	"value" text
);