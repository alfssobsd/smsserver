class CreateChannelPermissionTypes < ActiveRecord::Migration
  def change
    create_table :channel_permission_types do |t|
      t.string :name
      t.timestamps
    end
  end
end
