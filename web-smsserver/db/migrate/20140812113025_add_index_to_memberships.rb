class AddIndexToMemberships < ActiveRecord::Migration
  def change
    add_index :memberships, [:channel_id, :member_id], unique: true
  end
end
