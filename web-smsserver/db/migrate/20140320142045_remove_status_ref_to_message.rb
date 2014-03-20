class RemoveStatusRefToMessage < ActiveRecord::Migration
  def change
    remove_reference :messages, :status, index: true
  end
end
