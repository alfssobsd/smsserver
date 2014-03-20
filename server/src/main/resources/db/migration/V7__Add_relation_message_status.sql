ALTER TABLE "public"."messages" DROP COLUMN "status";
ALTER TABLE "public"."messages" ADD COLUMN "status_id" int4;
ALTER TABLE "public"."messages" ADD CONSTRAINT "messages_status_id_fkey" FOREIGN KEY ("status_id") REFERENCES "public"."status" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;