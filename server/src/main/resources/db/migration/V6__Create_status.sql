CREATE SEQUENCE public.status_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE "public"."status" (
	"id" int4 PRIMARY KEY NOT NULL,
	"name" varchar(255)
);
CREATE UNIQUE INDEX unq_status_name ON status USING BTREE (name);