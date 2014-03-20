CREATE SEQUENCE public.channel_connection_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE "public"."channel_connections" (
  "id" int4 PRIMARY KEY NOT NULL,
  channel_id integer NOT NULL,
  smpp_system_type character varying(255),
  FOREIGN KEY (channel_id) REFERENCES channels (id)
);

INSERT INTO "public"."channel_connections" (id, channel_id, smpp_system_type) VALUES (1, 1, 'SINGLE');
ALTER TABLE "public"."channels" DROP COLUMN "smpp_system_type";
