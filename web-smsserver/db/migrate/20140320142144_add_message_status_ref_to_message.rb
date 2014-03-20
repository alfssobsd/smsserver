class AddMessageStatusRefToMessage < ActiveRecord::Migration
  def change
    add_reference :messages, :message_status, index: true
  end
end
