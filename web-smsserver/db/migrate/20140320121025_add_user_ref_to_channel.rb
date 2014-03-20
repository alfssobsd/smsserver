class AddUserRefToChannel < ActiveRecord::Migration
  def change
    add_reference :channels, :user, index: true
  end
end
