class AddStatusRefToMessage < ActiveRecord::Migration
  def change
    add_reference :messages, :status, index: true
  end
end
