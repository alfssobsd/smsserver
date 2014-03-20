class CreateMessages < ActiveRecord::Migration
  def change
    create_table :messages do |t|
      t.string :message_id
      t.string :from
      t.string :to
      t.boolean :is_payload, default: false
      t.integer :sequence_number
      t.integer :esm_class
      t.binary  :message_data
      t.datetime :create_datetime
      t.datetime :update_datetime
      t.string   :queue_name
      t.integer  :send_retry

      t.timestamps
    end
  end
end
