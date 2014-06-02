class AddErrorCodeToMessage < ActiveRecord::Migration
  def change
    add_column :messages, :error_code, :integer

  end
end
