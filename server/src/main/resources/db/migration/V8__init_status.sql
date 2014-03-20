insert into "public"."status" ( "id", "name") values ( 1, 'WAITING_SEND');
insert into "public"."status" ( "id", "name") values ( 2, 'WAITING_RESPONSE');
insert into "public"."status" ( "id", "name") values ( 3, 'WAITING_DELIVERY');
insert into "public"."status" ( "id", "name") values ( 4, 'FAIL');
insert into "public"."status" ( "id", "name") values ( 5, 'SUCCESS');
ALTER SEQUENCE "public"."status_seq" RESTART 6;