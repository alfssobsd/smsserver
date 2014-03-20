class InitMessageStatus < ActiveRecord::Migration
  def migrate(direction)
    super
    if direction == :up
      MessageStatus.create!(name: "WAITING_SEND")
      MessageStatus.create!(name: "WAITING_RESPONSE")
      MessageStatus.create!(name: "WAITING_DELIVERY")
      MessageStatus.create!(name: "FAIL")
      MessageStatus.create!(name: "SUCCESS")
    end
  end

  def change
  end
end
