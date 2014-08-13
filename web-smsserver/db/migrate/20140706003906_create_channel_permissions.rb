class CreateChannelPermissions < ActiveRecord::Migration
  def change
    create_table :channel_permissions do |t|
      t.references :channel
      t.references :channel_permission_type
      t.references :user
      t.timestamps
    end
  end
end
