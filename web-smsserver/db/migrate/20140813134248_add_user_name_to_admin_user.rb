class AddUserNameToAdminUser < ActiveRecord::Migration
  def migrate(direction)
    super
    if direction == :up
      user = User.find_by_email('admin@example.com')
      user.username = 'admin'
      user.save
    end
  end

  def change
  end
end
