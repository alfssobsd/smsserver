class AddUserToken < ActiveRecord::Migration
  def change
    add_column :users, :token, :string, default: nil, null: true
  end
end
