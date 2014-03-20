CREATE TABLE public.auth_users_channels (
  auth_user_id integer NOT NULL,
  channel_id integer NOT NULL,
  PRIMARY KEY (channel_id, auth_user_id),
  FOREIGN KEY (channel_id) REFERENCES channels (id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (auth_user_id) REFERENCES auth_users (id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);