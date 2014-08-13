class AddDefaultValueToChannel < ActiveRecord::Migration
  def change
    change_column :channels, :smpp_host, :string, default: "localhost"
    change_column :channels, :smpp_port, :integer, default: 3700
    change_column :channels, :smpp_username, :string, default: "username"
    change_column :channels, :smpp_password, :string, default: "password"
    change_column :channels, :smpp_source_addr, :string, default: "source_addr"
    change_column :channels, :smpp_source_ton, :integer, default: 5
    change_column :channels, :smpp_source_npi, :integer, default: 1
    change_column :channels, :smpp_dest_ton, :integer, default: 1
    change_column :channels, :smpp_dest_npi, :integer, default: 1
    change_column :channels, :smpp_max_split_message, :integer, default: 12
    change_column :channels, :smpp_max_message_per_second, :integer, default: 20
    change_column :channels, :smpp_reconnect_timeout, :integer, default: 30
    change_column :channels, :smpp_enquire_link_interval, :integer, default: 60
  end
end
