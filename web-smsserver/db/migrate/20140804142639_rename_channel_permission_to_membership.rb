class RenameChannelPermissionToMembership < ActiveRecord::Migration
  def change
    rename_table :channel_permissions, :memberships
    rename_column :memberships, :user_id, :member_id
  end
end
