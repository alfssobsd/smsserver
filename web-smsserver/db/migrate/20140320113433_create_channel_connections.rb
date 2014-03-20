class CreateChannelConnections < ActiveRecord::Migration
  def change
    create_table :channel_connections do |t|
      t.string :smpp_system_type

      t.timestamps
    end
  end
end
