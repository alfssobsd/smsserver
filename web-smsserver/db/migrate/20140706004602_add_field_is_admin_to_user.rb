class AddFieldIsAdminToUser < ActiveRecord::Migration

  def migrate(direction)
    super
    user = User.find(1)
    user.is_admin = true
    user.save
  end
  def change
    add_column :users, :is_admin, :boolean, default: false
  end
end
