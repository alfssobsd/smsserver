# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20140812113025) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "channel_connections", force: true do |t|
    t.string   "smpp_system_type"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "channel_id"
  end

  add_index "channel_connections", ["channel_id"], name: "index_channel_connections_on_channel_id", using: :btree

  create_table "channel_permission_types", force: true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "channels", force: true do |t|
    t.string   "name"
    t.string   "queue_name"
    t.string   "smpp_host",                   default: "localhost"
    t.integer  "smpp_port",                   default: 3700
    t.string   "smpp_username",               default: "username"
    t.string   "smpp_password",               default: "password"
    t.string   "smpp_source_addr",            default: "source_addr"
    t.integer  "smpp_source_ton",             default: 5
    t.integer  "smpp_source_npi",             default: 1
    t.integer  "smpp_dest_ton",               default: 1
    t.integer  "smpp_dest_npi",               default: 1
    t.integer  "smpp_max_split_message",      default: 12
    t.integer  "smpp_max_message_per_second", default: 20
    t.integer  "smpp_reconnect_timeout",      default: 30
    t.integer  "smpp_enquire_link_interval",  default: 60
    t.boolean  "is_payload",                  default: false
    t.boolean  "is_fake",                     default: false
    t.boolean  "is_enable",                   default: true
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "memberships", force: true do |t|
    t.integer  "channel_id"
    t.integer  "channel_permission_type_id"
    t.integer  "member_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "memberships", ["channel_id", "member_id"], name: "index_memberships_on_channel_id_and_member_id", unique: true, using: :btree

  create_table "message_statuses", force: true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "messages", force: true do |t|
    t.string   "message_id"
    t.string   "addr_from"
    t.string   "addr_to"
    t.boolean  "is_payload",        default: false
    t.integer  "sequence_number"
    t.integer  "esm_class"
    t.binary   "message_data"
    t.datetime "create_datetime"
    t.datetime "update_datetime"
    t.string   "queue_name"
    t.integer  "send_retry"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "channel_id"
    t.integer  "message_status_id"
    t.integer  "error_code"
  end

  add_index "messages", ["channel_id"], name: "index_messages_on_channel_id", using: :btree
  add_index "messages", ["message_status_id"], name: "index_messages_on_message_status_id", using: :btree

  create_table "users", force: true do |t|
    t.string   "email",                  default: "",    null: false
    t.string   "encrypted_password",     default: "",    null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,     null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.boolean  "is_admin",               default: false
    t.string   "token"
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree

end
