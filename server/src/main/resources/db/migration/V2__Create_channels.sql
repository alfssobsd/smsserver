CREATE SEQUENCE public.channel_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE public.channels (
  id integer PRIMARY KEY NOT NULL,
  is_enable boolean,
  is_fake boolean,
  name character varying(255) NOT NULL,
  is_payload boolean,
  queue character varying(255) NOT NULL,
  smpp_dest_npi integer NOT NULL,
  smpp_dest_ton integer NOT NULL,
  smpp_enquire_link_interval integer,
  smpp_host character varying(255) NOT NULL,
  smpp_password character varying(255) NOT NULL,
  smpp_port integer NOT NULL,
  smpp_reconnect_timeout integer,
  smpp_send_message_per_second integer,
  smpp_source_addr character varying(255),
  smpp_source_npi integer NOT NULL,
  smpp_source_ton integer NOT NULL,
  smpp_system_type character varying(255),
  smpp_username character varying(255) NOT NULL,
  smpp_max_message integer NOT NULL
);
CREATE UNIQUE INDEX unq_channels_name ON channels USING BTREE (name);