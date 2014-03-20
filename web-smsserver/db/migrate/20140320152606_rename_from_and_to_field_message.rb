class RenameFromAndToFieldMessage < ActiveRecord::Migration
  def change
    rename_column :messages, :to, :addr_to
    rename_column :messages, :from, :addr_from

  end
end
