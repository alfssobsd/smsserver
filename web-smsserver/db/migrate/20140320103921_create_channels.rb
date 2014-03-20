class CreateChannels < ActiveRecord::Migration
  def change
    create_table :channels do |t|
      t.string :name
      t.string :queue_name
      t.string :smpp_host
      t.integer :smpp_port
      t.string :smpp_username
      t.string :smpp_password
      t.string :smpp_source_addr
      t.integer :smpp_source_ton
      t.integer :smpp_source_npi
      t.integer :smpp_dest_ton
      t.integer :smpp_dest_npi
      t.integer :smpp_max_split_message
      t.integer :smpp_max_message_per_second
      t.integer :smpp_reconnect_timeout
      t.integer :smpp_enquire_link_interval
      t.boolean :is_payload, default:false
      t.boolean :is_fake, default:false
      t.boolean :is_enable, default:true


      t.timestamps
    end
  end
end
