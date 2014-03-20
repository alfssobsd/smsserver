CREATE SEQUENCE public.message_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE public.messages (
  id integer PRIMARY KEY NOT NULL,
  create_datetime timestamp without time zone NOT NULL,
  esm_class smallint,
  send_from character varying(255),
  message_data bytea NOT NULL,
  message_id character varying(255),
  is_payload boolean,
  queue_name character varying(255),
  sequence_number integer,
  status character varying(255) NOT NULL,
  send_to character varying(255) NOT NULL,
  update_datetime timestamp without time zone NOT NULL,
  channel_id integer,
  FOREIGN KEY (channel_id) REFERENCES channels (id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);