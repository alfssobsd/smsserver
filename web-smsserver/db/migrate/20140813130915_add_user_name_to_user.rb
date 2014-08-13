class AddUserNameToUser < ActiveRecord::Migration
  def change
    add_column :users, :username, :string
    add_index :users, [:username, :email], unique: true
    add_index :users, :username, unique: true
  end
end
