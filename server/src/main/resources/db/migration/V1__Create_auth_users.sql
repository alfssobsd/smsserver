CREATE SEQUENCE public.auth_users_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE public.auth_users (
  id integer PRIMARY KEY NOT NULL,
  login character varying(255) NOT NULL,
  password character varying(255) NOT NULL
);
CREATE UNIQUE INDEX unq_auth_user_login ON auth_users USING BTREE (login);