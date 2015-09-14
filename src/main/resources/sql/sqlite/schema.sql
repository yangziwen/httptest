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

DROP TABLE IF EXISTS "case_param";
CREATE TABLE "case_param" (
	"id" integer PRIMARY KEY AUTOINCREMENT,
	"case_id" integer NOT NULL,
	"type" integer DEFAULT 0,
	"name" varchar(40),
	"value" text
);