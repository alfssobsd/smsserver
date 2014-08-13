class AddIndexToChannel < ActiveRecord::Migration
  def change
    add_index :channels, :name, unique: true
    add_index :channels, [:name, :queue_name], unique: true
  end
end
