class AddChannelRefToChannelConnection < ActiveRecord::Migration
  def change
    add_reference :channel_connections, :channel, index: true
  end
end
