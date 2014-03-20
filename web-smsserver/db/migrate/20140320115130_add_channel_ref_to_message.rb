class AddChannelRefToMessage < ActiveRecord::Migration
  def change
    add_reference :messages, :channel, index: true
  end
end
