class AddChannelPermissionTypes < ActiveRecord::Migration
  def migrate(direction)
    super
    if direction == :up
      ChannelPermissionType.create!(name: "member")
      ChannelPermissionType.create!(name: "manager")
    end
  end
  def change
  end
end
